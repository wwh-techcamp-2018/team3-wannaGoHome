package wannagohome.exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {

    @ExceptionHandler(UnAuthorizedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleUnAuthorizedException(UnAuthorizedException exception, Model model) {
        model.addAttribute("code", HttpStatus.NOT_FOUND.value());
        model.addAttribute("description", "WAS FORBIDDEN");
        return "error";
    }

    @ExceptionHandler(ErrorEntityException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUnAuthorizedException(ErrorEntityException exception, Model model) {
        model.addAttribute("code", HttpStatus.NOT_FOUND.value());
        model.addAttribute("description", "WAS NOT FOUND");
        return "error";
    }
}
