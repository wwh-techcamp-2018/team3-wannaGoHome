package wannagohome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wannagohome.domain.*;
import wannagohome.exception.NotFoundException;
import wannagohome.repository.CardRepository;
import wannagohome.repository.CommentRepository;
import wannagohome.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CommentRepository commentRepository;

    public List<Card> findCardsByUser(User user) {
        return cardRepository.findAllByAuthorAndDeletedFalse(user);
    }

    public List<Card> findDueCardsByUser(User user) {
        return cardRepository.findAllByAuthorAndDeletedFalseAndEndDateIsNotNull(user);
    }

    public Card setCardDueDate(Long id, CardDetailDto cardDetailDto) {
        Card card = cardRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(ErrorType.CARD_ID, "일치하는 카드가 없습니다."));
        card.setEndDate(cardDetailDto.getEndDate());

        return cardRepository.save(card);
    }

    public Card setCardLabel(Long id, CardDetailDto cardDetailDto) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorType.CARD_ID, "일치하는 카드가 없습니다."));
        card.setLabels(cardDetailDto.getLabels());

        return cardRepository.save(card);
    }

    @Transactional
    public User assignCardToUser(User user, Long cardId, CardDetailDto dto) {
        Card card = findCardById(cardId);
        User assignee = userRepository.findById(dto.getUserId()).orElseThrow(() -> new NotFoundException(ErrorType.USER_ID, "없는 유저 아이디 입니다."));

        card.addAssignee(assignee);
        return assignee;
    }

    @Transactional
    public User dischargeCardFromUser(Long cardId, CardDetailDto dto) {
        Card card = findCardById(cardId);
        User assignee = userRepository.findById(dto.getUserId()).orElseThrow(() -> new NotFoundException(ErrorType.USER_ID, "없는 유저 아이디 입니다."));

        card.dischargeAssignee(assignee);
        return assignee;
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

    private Card findCardById(Long id) {
        return cardRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorType.CARD_ID, "없는 카드 아이디 입니다."));
    }

}
