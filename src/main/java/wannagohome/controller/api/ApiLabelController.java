package wannagohome.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wannagohome.domain.Card;
import wannagohome.domain.Label;
import wannagohome.domain.User;
import wannagohome.interceptor.LoginUser;
import wannagohome.service.LabelService;

import java.util.List;

@RestController
@RequestMapping("/api/labels")
public class ApiLabelController {

    @Autowired
    private LabelService labelService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<Label> readLabels() {
        return labelService.findAll();
    }



}
