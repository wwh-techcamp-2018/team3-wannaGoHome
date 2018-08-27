package wannagohome.exception;

import wannagohome.domain.error.ErrorType;

public class UnAuthenticationException extends ErrorEntityException {

    public UnAuthenticationException(ErrorType errorType, String message) {
        super(errorType, message);
    }
}
