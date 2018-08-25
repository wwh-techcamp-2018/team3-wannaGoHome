package wannagohome.exception;

import wannagohome.domain.ErrorType;

public class BadRequestException extends ErrorEntityException {

    public BadRequestException(ErrorType errorType, String message) {
        super(errorType, message);
    }
}
