package wannagohome.domain;

import java.util.Arrays;

public enum ErrorType {
    USER_EMAIL("email"),
    USER_NAME("name"),
    USER_PASSWORD("password");

    private String errorType;

    ErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorType() {
        return errorType;
    }

    public static ErrorType of(String errorType) {
        return Arrays.stream(ErrorType.values())
                .filter(e -> e.getErrorType().equals(errorType))
                .findFirst()
                .orElseThrow(RuntimeException::new);
        // TODO: Exception customize
    }
}