package wannagohome.exception;

import wannagohome.domain.error.ErrorType;

public class DuplicationException extends ErrorEntityException {

    public DuplicationException(ErrorType errorType, String message) {
        super(errorType, message);
    }
}
