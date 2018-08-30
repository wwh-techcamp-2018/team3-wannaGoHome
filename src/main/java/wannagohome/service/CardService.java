package wannagohome.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wannagohome.domain.activity.ActivityType;
import wannagohome.domain.activity.CardActivity;
import wannagohome.domain.board.Board;
import wannagohome.domain.card.*;
import wannagohome.domain.error.ErrorType;
import wannagohome.domain.file.Attachment;
import wannagohome.domain.user.User;
import wannagohome.domain.user.UserIncludedInBoard;
import wannagohome.event.BoardEvent;
import wannagohome.exception.NotFoundException;
import wannagohome.repository.*;
import wannagohome.service.file.UploadService;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardService {
    private static final Logger log = LoggerFactory.getLogger(CardService.class);

    @Autowired
    private UserIncludedInBoardRepository userIncludedInBoardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Resource(name = "fileUploadService")
    private UploadService uploadService;

    public List<Card> findCardsByUser(User user) {
        return cardRepository.findAllByAuthorAndDeletedFalse(user);
    }

    public List<Card> findDueCardsByUser(User user) {
        return cardRepository.findAllByAuthorAndDeletedFalseAndEndDateIsNotNull(user);
    }

    public Card setCardDueDate(User user, Long id, CardDetailDto cardDetailDto) {
        Card card = findCardById(id);
        card.setEndDate(cardDetailDto.getEndDate());
        card = cardRepository.save(card);

        CardActivity activity = CardActivity.valueOf(user, card, ActivityType.CARD_UPDATE_DUE_DATE);
        applicationEventPublisher.publishEvent(new BoardEvent(this, activity));
        notifyBoardRefresh(card.getBoard());
        return card;
    }

    public Card setCardLabel(User user, Long id, CardDetailDto cardDetailDto) {
        Card card = findCardById(id);
        card.setLabels(cardDetailDto.getLabels());
        card = cardRepository.save(card);

        CardActivity activity = CardActivity.valueOf(user, card, ActivityType.CARD_UPDATE_LABEL);
        applicationEventPublisher.publishEvent(new BoardEvent(this, activity));
        notifyBoardRefresh(card.getBoard());
        return card;
    }

    public Card updateCardDescription(User user, Long cardId, CardDetailDto dto) {
        Card card = findCardById(cardId);
        card.updateDescription(dto);
        card = cardRepository.save(card);

        CardActivity activity = CardActivity.valueOf(user, card, ActivityType.CARD_UPDATE_DESCRIPTION);
        applicationEventPublisher.publishEvent(new BoardEvent(this, activity));
        return card;
    }

    public List<AssigneeDto> assignCardToUser(User user, Long cardId, CardDetailDto dto) {
        Card card = findCardById(cardId);
        User assignee = userRepository.findById(dto.getUserId()).orElseThrow(() -> new NotFoundException(ErrorType.USER_ID, "없는 유저 아이디 입니다."));
        card.addAssignee(assignee);
        cardRepository.save(card);

        CardActivity activity = CardActivity.valueOf(user, card, ActivityType.CARD_ASSIGN, assignee);
        applicationEventPublisher.publishEvent(new BoardEvent(this, activity));
        return userIncludedInBoardRepository.findAllByBoard(card.getBoard()).stream()
                .map(userInBoard -> AssigneeDto.valueOf(userInBoard.getUser(), card)).collect(Collectors.toList());
    }

    public List<AssigneeDto> dischargeCardFromUser(Long cardId, CardDetailDto dto) {
        Card card = findCardById(cardId);
        User assignee = userRepository.findById(dto.getUserId()).orElseThrow(() -> new NotFoundException(ErrorType.USER_ID, "없는 유저 아이디 입니다."));
        card.dischargeAssignee(assignee);
        cardRepository.save(card);

        return userIncludedInBoardRepository.findAllByBoard(card.getBoard()).stream()
                .map(userInBoard -> AssigneeDto.valueOf(userInBoard.getUser(), card)).collect(Collectors.toList());
    }

    public Comment addComment(User user, Long cardId, CommentDto dto) {
        Card card = findCardById(cardId);
        Comment comment = Comment.valueOf(dto, user, card);
        comment = commentRepository.save(comment);

        CardActivity activity = CardActivity.valueOf(user, card, ActivityType.CARD_UPDATE_COMMENT);
        applicationEventPublisher.publishEvent(new BoardEvent(this, activity));
        return comment;
    }

    public Comment removeComment(User user, Long cardId, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException(ErrorType.COMMENT_ID, "없는 댓글 아이디 입니다."));
        comment.delete(user);
        return commentRepository.save(comment);
    }

    public List<Comment> getComments() {
        return commentRepository.findAllByDeletedFalse();
    }

    public CardDetailDto getCardDetail(Long id) {
        Card card = findCardById(id);
        return CardDetailDto.valueOf(card, labelRepository.findAll());
    }

    public Card findCardById(Long id) {
        return cardRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException(ErrorType.CARD_ID, "없는 카드 아이디 입니다."));
    }

    public List<Label> addLabel(User user, Long cardId, Label label) {
        Card card = findCardById(cardId);
        card.getLabels().add(label);
        card = cardRepository.save(card);

        CardActivity activity = CardActivity.valueOf(user, card, ActivityType.CARD_UPDATE_LABEL);
        applicationEventPublisher.publishEvent(new BoardEvent(this, activity));
        notifyBoardRefresh(card.getBoard());
        return card.getLabels();
    }

    public List<Label> getLabels(Long cardId) {
        Card card = findCardById(cardId);
        return card.getLabels();
    }

    @Transactional
    public List<Label> deleteLabel(Long cardId, Long labelId) {
        Card card = findCardById(cardId);
        Label getLabel = labelRepository.findById(labelId).orElseThrow(()->new NotFoundException(ErrorType.LABEL_ID, "일치하는 라벨이 없습니다."));
        card.removeLabel(getLabel);
        notifyBoardRefresh(card.getBoard());
        return card.getLabels();
    }

    public List<AssigneeDto> getMembers(Long cardId, String keyword) {
        Card card = findCardById(cardId);
        List<UserIncludedInBoard> users = userIncludedInBoardRepository.findAllByBoardAndUserNameContainsIgnoreCase(card.getBoard(), keyword);
        return users.stream()
                .map(userIncludedInBoard -> AssigneeDto.valueOf(userIncludedInBoard.getUser(), card))
                .collect(Collectors.toList());
    }

    public Card updateCardDate(User user, Long id, CardDetailDto cardDetailDto) {
        Card card = findCardById(id);
        card.setCreateDate(cardDetailDto.getCreateDate());
        card.setEndDate(cardDetailDto.getEndDate());
        card = cardRepository.save(card);

        CardActivity activity = CardActivity.valueOf(user, card, ActivityType.CARD_UPDATE_DUE_DATE);
        applicationEventPublisher.publishEvent(new BoardEvent(this, activity));
        notifyBoardRefresh(card.getBoard());
        return card;
    }

    @Transactional
    public Card deleteCard(Long cardId) {
        Card card = findCardById(cardId);
        card.delete();
        notifyBoardRefresh(card.getBoard());
        return card;
    }

    @Transactional
    public Card deleteCardDate(Long id) {
        Card card = findCardById(id);
        card.removeDueDate();
        card = cardRepository.save(card);
        notifyBoardRefresh(card.getBoard());
        return card;
    }

    public CardDetailDto updateCardTitle(User user, Long cardId, CardDetailDto dto) {
        Card card = findCardById(cardId);
        card.setTitle(dto.getCardTitle());
        cardRepository.save(card);

        notifyBoardRefresh(card.getBoard());
        return dto;
    }

    private void notifyBoardRefresh(Board board) {
        simpMessageSendingOperations.convertAndSend("/topic/board/" + board.getId(), board.getBoardDto());
    }

    @Transactional
    public List<Attachment> addFile(Long cardId, MultipartFile file) {
        Card card = findCardById(cardId);
        Attachment attachment = new Attachment(card,file.getOriginalFilename(), uploadService.fileUpload(file));
        attachmentRepository.save(attachment);
        return card.getAttachments();
    }

    public List<Attachment> deleteFile(Long cardId, Long fileId) {
        Attachment attachment = findAttachmentById(fileId);
        uploadService.fileDelete(attachment.getLink());
        attachmentRepository.delete(attachment);
        return findCardById(cardId).getAttachments();
    }

    public Attachment findAttachmentById(Long fileId) {
        return attachmentRepository.findById(fileId).orElseThrow(()->new NotFoundException(ErrorType.FILE_ID, "일치하는 파일이 없습니다."));
    }
}
