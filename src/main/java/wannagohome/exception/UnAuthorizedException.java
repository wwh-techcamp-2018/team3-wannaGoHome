package wannagohome.exception;

import wannagohome.domain.error.ErrorType;

public class UnAuthorizedException extends ErrorEntityException {

    public UnAuthorizedException(ErrorType errorType, String message) {
        super(errorType, message);
    }
}
