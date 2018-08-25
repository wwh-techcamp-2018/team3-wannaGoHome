package wannagohome.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import wannagohome.domain.Card;
import wannagohome.domain.CardDetailDto;
import wannagohome.domain.User;
import wannagohome.interceptor.LoginUser;
import wannagohome.service.CardService;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class ApiCardController {

    @Autowired
    CardService cardService;

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
    public User assignCardToUser(@RequestBody CardDetailDto cardDetail) {
        return null;
    }

    @DeleteMapping("/{cardId}/assign")
    public User dischargeCardFromUser(@RequestBody CardDetailDto cardDetail) {
        return null;
    }


}
