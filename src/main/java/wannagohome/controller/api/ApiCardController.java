package wannagohome.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import wannagohome.domain.*;
import wannagohome.interceptor.LoginUser;
import wannagohome.service.CardService;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class ApiCardController {

    @Autowired
    private CardService cardService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<Card> readCards(@LoginUser User user) {
        return cardService.findCardsByUser(user);
    }

    @GetMapping("/calendar")
    @ResponseStatus(HttpStatus.OK)
    public List<Card> readDueCards(@LoginUser User user) {
        return cardService.findDueCardsByUser(user);
    }

    @PostMapping("/{cardId}/assign")
    public User assignCardToUser(@LoginUser User user, @PathVariable Long cardId, @RequestBody CardDetailDto cardDetail) {
        return cardService.assignCardToUser(user, cardId, cardDetail);
    }

    @DeleteMapping("/{cardId}/assign")
    public User dischargeCardFromUser(@PathVariable Long cardId, @RequestBody CardDetailDto cardDetail) {
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
}
