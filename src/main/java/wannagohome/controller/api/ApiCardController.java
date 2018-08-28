package wannagohome.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import wannagohome.domain.card.*;
import wannagohome.domain.user.User;
import wannagohome.interceptor.LoginUser;
import wannagohome.service.CardService;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class ApiCardController {
    private static final Logger log = LoggerFactory.getLogger(ApiCardController.class);

    @Autowired
    private CardService cardService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<Card> readCards(@LoginUser User user) {
        return cardService.findCardsByUser(user);
    }

    @DeleteMapping("/{cardId}")
    public Card deleteCard(@PathVariable Long cardId) {
        return cardService.deleteCard(cardId);
    }

    @GetMapping("/calendar")
    @ResponseStatus(HttpStatus.OK)
    public List<Card> readDueCards(@LoginUser User user) {
        return cardService.findDueCardsByUser(user);
    }

    @PostMapping("/{id}/date")
    @ResponseStatus(HttpStatus.CREATED)
    public Card setCardDueDate(@LoginUser User user, @PathVariable Long id, @RequestBody CardDetailDto cardDetailDto) {
        return cardService.setCardDueDate(user, id, cardDetailDto);
    }

    @PutMapping("/{id}/date")
    public Card updateCardDate(@LoginUser User user, @PathVariable Long id, @RequestBody CardDetailDto cardDetailDto) {
        return cardService.updateCardDate(user, id, cardDetailDto);
    }

    @DeleteMapping("/{id}/date")
    public Card deleteCardDate(@PathVariable Long id) {
        return cardService.deleteCardDate(id);
    }

    @PostMapping("/details/label/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Card setCardLabel(@LoginUser User user, @PathVariable Long id, @RequestBody CardDetailDto cardDetailDto) {
        return cardService.setCardLabel(user, id, cardDetailDto);
    }

    @PostMapping("/{cardId}/description")
    public Card updateCardDescription(@LoginUser User user, @PathVariable Long cardId, @RequestBody CardDetailDto cardDetailDto) {
        return cardService.updateCardDescription(user, cardId, cardDetailDto);
    }

    @PutMapping("/{cardId}/title")
    public CardDetailDto updateCardTitle(@LoginUser User user, @PathVariable Long cardId, @RequestBody CardDetailDto cardDetailDto) {
        return cardService.updateCardTitle(user, cardId, cardDetailDto);
    }

    @PostMapping("/{cardId}/assign")
    public List<AssigneeDto> assignCardToUser(@LoginUser User user, @PathVariable Long cardId, @RequestBody CardDetailDto cardDetail) {
        return cardService.assignCardToUser(user, cardId, cardDetail);
    }

    @DeleteMapping("/{cardId}/assign")
    public List<AssigneeDto> dischargeCardFromUser(@PathVariable Long cardId, @RequestBody CardDetailDto cardDetail) {
        return cardService.dischargeCardFromUser(cardId, cardDetail);
    }

    @PostMapping("/{cardId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public Comment addComment(@LoginUser User user, @PathVariable Long cardId, @RequestBody CommentDto dto) {
        return cardService.addComment(user, cardId, dto);
    }

    @DeleteMapping("/{cardId}/comments/{commentId}")
    public Comment removeComment(@LoginUser User user, @PathVariable Long cardId, @PathVariable Long commentId) {
        return cardService.removeComment(user, cardId, commentId);
    }

    @GetMapping("/{cardId}/comments")
    public List<Comment> getComments() {
        return cardService.getComments();
    }

    @GetMapping("/{cardId}")
    public CardDetailDto getCard(@PathVariable Long cardId) {
        return cardService.getCardDetail(cardId);
    }

    @GetMapping("/{cardId}/label")
    @ResponseStatus(HttpStatus.OK)
    public List<Label> getLabels(@PathVariable Long cardId) {
        return cardService.getLabels(cardId);
    }

    @PostMapping("/{cardId}/label")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Label> addLabel(@LoginUser User user, @PathVariable Long cardId, @RequestBody Label label) {
        return cardService.addLabel(user, cardId, label);
    }

    @DeleteMapping("/{cardId}/label")
    public List<Label> deleteLabel(@PathVariable Long cardId, @RequestBody Label label) {
        return cardService.deleteLabel(cardId, label.getId());
    }

    @GetMapping("/{cardId}/members")
    public List<AssigneeDto> getMembers(@PathVariable Long cardId, @RequestParam String keyword) {
        return cardService.getMembers(cardId, keyword);
    }
}
