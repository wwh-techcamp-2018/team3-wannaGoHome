package wannagohome.exception;

import wannagohome.domain.error.ErrorType;

public class UnsupportedFileFormatException extends ErrorEntityException {
    public UnsupportedFileFormatException(ErrorType errorType, String message) {
        super(errorType, message);
    }
}
