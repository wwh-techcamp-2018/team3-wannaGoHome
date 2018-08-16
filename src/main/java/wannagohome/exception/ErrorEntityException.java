package wannagohome.exception;

import wannagohome.domain.ErrorEntity;
import wannagohome.domain.ErrorType;

public class ErrorEntityException extends RuntimeException {

    protected ErrorType errorType;

    public ErrorEntityException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public ErrorEntity entity() {
        return new ErrorEntity(errorType, getMessage());
    }
}
