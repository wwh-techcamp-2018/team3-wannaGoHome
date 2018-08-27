package wannagohome.exception;

import wannagohome.domain.error.ErrorType;

public class EmptyFileUploadedException extends ErrorEntityException {
    public EmptyFileUploadedException(ErrorType errorType, String message) {
        super(errorType, message);
    }
}
