package wannagohome.controller.web;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Controller
public class ErrorHandleController implements ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (!Objects.isNull(status)) {
            int statusCode = Integer.valueOf(status.toString());
            model.addAttribute("code", statusCode);
            if (statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("description", "WAS FORBIDDEN");
            }

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("description", "WAS NOT FOUND");
            }

            if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("description", "WAS NOT IMPLEMENTED");
            }
        }
        return "error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
