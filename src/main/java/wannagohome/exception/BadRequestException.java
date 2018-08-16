package wannagohome.exception;

import wannagohome.domain.ErrorEntity;
import wannagohome.domain.ErrorType;

public class BadRequestException extends RuntimeException {

    private ErrorType errorType;

    public BadRequestException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public ErrorEntity entity() {
        return new ErrorEntity(errorType, getMessage());
    }
}
