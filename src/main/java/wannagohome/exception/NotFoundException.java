package wannagohome.exception;

import wannagohome.domain.error.ErrorType;

public class NotFoundException extends ErrorEntityException {

    public NotFoundException(ErrorType errorType, String message) {
        super(errorType, message);
    }
}
