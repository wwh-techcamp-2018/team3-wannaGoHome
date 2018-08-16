package wannagohome.exception;

import wannagohome.domain.ErrorEntity;
import wannagohome.domain.ErrorType;

public class BadRequestException extends ErrorEntityException {

    public BadRequestException(ErrorType errorType, String message) {
        super(errorType, message);
    }

    public ErrorEntity entity() {
        return new ErrorEntity(errorType, getMessage());
    }
}
