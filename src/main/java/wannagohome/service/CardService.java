package wannagohome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wannagohome.domain.card.*;
import wannagohome.domain.error.ErrorType;
import wannagohome.domain.user.User;
import wannagohome.domain.user.UserIncludedInBoard;
import wannagohome.exception.NotFoundException;
import wannagohome.repository.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardService {

    @Autowired
    private UserIncludedInBoardRepository userIncludedInBoardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private LabelRepository labelRepository;

    public List<Card> findCardsByUser(User user) {
        return cardRepository.findAllByAuthorAndDeletedFalse(user);
    }

    public List<Card> findDueCardsByUser(User user) {
        return cardRepository.findAllByAuthorAndDeletedFalseAndEndDateIsNotNull(user);
    }

    public Card setCardDueDate(Long id, CardDetailDto cardDetailDto) {
        Card card = findCardById(id);
        card.setEndDate(cardDetailDto.getEndDate());
        return cardRepository.save(card);
    }

    public Card setCardLabel(Long id, CardDetailDto cardDetailDto) {
        Card card = findCardById(id);
        card.setLabels(cardDetailDto.getLabels());

        return cardRepository.save(card);
    }

    public Card updateCardDescription(User user, Long cardId, CardDetailDto dto) {
        Card card = findCardById(cardId);
        card.updateDescription(dto);
        return cardRepository.save(card);
    }

    @Transactional
    public List<AssigneeDto> assignCardToUser(User user, Long cardId, CardDetailDto dto) {
        Card card = findCardById(cardId);
        User assignee = userRepository.findById(dto.getUserId()).orElseThrow(() -> new NotFoundException(ErrorType.USER_ID, "없는 유저 아이디 입니다."));

        card.addAssignee(assignee);
        return userIncludedInBoardRepository.findAllByBoard(card.getBoard()).stream()
                .map(userInBoard -> AssigneeDto.valueOf(userInBoard.getUser(), card)).collect(Collectors.toList());
    }

    @Transactional
    public List<AssigneeDto> dischargeCardFromUser(Long cardId, CardDetailDto dto) {
        Card card = findCardById(cardId);
        User assignee = userRepository.findById(dto.getUserId()).orElseThrow(() -> new NotFoundException(ErrorType.USER_ID, "없는 유저 아이디 입니다."));

        card.dischargeAssignee(assignee);
        return userIncludedInBoardRepository.findAllByBoard(card.getBoard()).stream()
                .map(userInBoard -> AssigneeDto.valueOf(userInBoard.getUser(), card)).collect(Collectors.toList());
    }

    @Transactional
    public Comment addComment(User user, Long cardId, CommentDto dto) {
        Card card = findCardById(cardId);
        Comment comment = Comment.valueOf(dto, user, card);

        return commentRepository.save(comment);
    }

    @Transactional
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
        return new CardDetailDto(
                card.getTitle(),
                card.getTask().getTitle(),
                card.getDescription(),
                card.getComments()
        );
    }

    public Card findCardById(Long id) {
        return cardRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorType.CARD_ID, "없는 카드 아이디 입니다."));
    }

    public List<CardLabelDto> addLabel(Long cardId, Label label) {
        Card card = findCardById(cardId);
        card.getLabels().add(label);
        cardRepository.save(card);

        return labelRepository.findAll().stream()
                .map(l -> CardLabelDto.valueOf(l, card))
                .collect(Collectors.toList());
    }

    public List<CardLabelDto> getLabels(Long cardId) {
        Card card = findCardById(cardId);
        return labelRepository.findAll().stream()
                .map(label -> CardLabelDto.valueOf(label, card))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<CardLabelDto> deleteLabel(Long cardId, Long labelId) {
        Card card = findCardById(cardId);
        Label getLabel = labelRepository.findById(labelId).orElseThrow(()->new NotFoundException(ErrorType.LABEL_ID, "일치하는 라벨이 없습니다."));
//        if(card.getLabels().contains(getLabel)) {
//            card.getLabels().remove(getLabel);
//        } else {
//            throw new NotFoundException(ErrorType.LABEL_ID, "일치하는 라벨이 없습니다.");
//        }
        card.removeLabel(getLabel);
        return labelRepository.findAll().stream()
                .map(label -> CardLabelDto.valueOf(label, card))
                .collect(Collectors.toList());
    }

    public List<AssigneeDto> getMembers(Long cardId, String keyword) {
        Card card = findCardById(cardId);
        List<UserIncludedInBoard> users = userIncludedInBoardRepository.findAllByBoardAndUserNameContains(card.getBoard(), keyword);
        return users.stream()
                .map(userIncludedInBoard -> AssigneeDto.valueOf(userIncludedInBoard.getUser(), card))
                .collect(Collectors.toList());
    }

    public Card updateCardDate(Long id, CardDetailDto cardDetailDto) {
        Card card = findCardById(id);
        card.setCreateDate(cardDetailDto.getCreateDate());
        card.setEndDate(cardDetailDto.getEndDate());
        return cardRepository.save(card);
    }
}
