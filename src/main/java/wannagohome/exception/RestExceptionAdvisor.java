package wannagohome.exception;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wannagohome.domain.error.ErrorEntity;
import wannagohome.domain.error.ErrorType;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestControllerAdvice(annotations = {RestController.class})
public class RestExceptionAdvisor {

    @Resource(name = "messageSourceAccessor")
    private MessageSourceAccessor messageSourceAccessor;

    @ExceptionHandler(UnAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public List<ErrorEntity> handleUnAuthenticationException(ErrorEntityException exception) {
        return Arrays.asList(exception.entity());
    }

    @ExceptionHandler(UnAuthorizedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public List<ErrorEntity> handleUnAuthorizedException(ErrorEntityException exception) {
        return Arrays.asList(exception.entity());
    }

    @ExceptionHandler(value = {BadRequestException.class, NotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorEntity> handleBadRequest(ErrorEntityException exception) {
        return Arrays.asList(exception.entity());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorEntity> handleValidationException(MethodArgumentNotValidException exception) {
        List<ErrorEntity> errors = new ArrayList<>();
        for (ObjectError objectError : exception.getBindingResult().getAllErrors()) {
            FieldError fieldError = (FieldError) objectError;
            errors.add(new ErrorEntity(ErrorType.of(fieldError.getField()), getErrorMessage(fieldError)));
        }
        return errors;
    }

    private String getErrorMessage(FieldError fieldError) {
        Optional<String> code = getFirstCode(fieldError);
        if (!code.isPresent())
            return null;
        String errorMessage = messageSourceAccessor.getMessage(code.get(), fieldError.getArguments(), fieldError.getDefaultMessage());
        return errorMessage;
    }

    private Optional<String> getFirstCode(FieldError fieldError) {
        String[] codes = fieldError.getCodes();
        if (codes.length == 0) {
            return Optional.empty();
        }
        return Optional.ofNullable(codes[0]);
    }
}
