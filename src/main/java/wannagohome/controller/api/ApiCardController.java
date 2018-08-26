package wannagohome.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import wannagohome.domain.Card;
import wannagohome.domain.CardDetailDto;
import wannagohome.domain.Team;
import wannagohome.domain.User;
import wannagohome.interceptor.LoginUser;
import wannagohome.service.CardService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class ApiCardController {
    private static final Logger log = LoggerFactory.getLogger(ApiCardController.class);

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

    @PostMapping("/details/date/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Card setCardDueDate(@LoginUser User user, @PathVariable Long id, @RequestBody CardDetailDto cardDetailDto) {
        return cardService.setCardDueDate(id, cardDetailDto);
    }

    @PostMapping("/details/label/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Card setCardLabel(@LoginUser User user, @PathVariable Long id, @RequestBody CardDetailDto cardDetailDto) {
        return cardService.setCardLabel(id, cardDetailDto);
    }


}
