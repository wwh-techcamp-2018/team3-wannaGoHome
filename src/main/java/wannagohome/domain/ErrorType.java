package wannagohome.domain;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum ErrorType {
    UNAUTHENTICATED("unauthenticated"),
    USER_EMAIL("email"),
    USER_NAME("name"),
    USER_PASSWORD("password");

    private String errorType;

    ErrorType(String errorType) {
        this.errorType = errorType;
    }

    @JsonValue
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
