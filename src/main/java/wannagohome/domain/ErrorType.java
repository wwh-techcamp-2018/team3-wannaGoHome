package wannagohome.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public enum ErrorType {
    UNAUTHENTICATED("unauthenticated"),
    USER_EMAIL("email"),
    USER_NAME("name"),
    USER_PASSWORD("password"),
    TEAM_NAME("name"),
    TEAM_DESC("description"),
    BOARD_ID("boardId"),
    BOARD_TITLE("title");
    private static final Logger log = LoggerFactory.getLogger(ErrorType.class);

    private String errorType;

    ErrorType(String errorType) {
        this.errorType = errorType;
    }

    @JsonValue
    public String getErrorType() {
        return errorType;
    }

    public static ErrorType of(String errorType) {
        log.debug("errorType: {}", errorType);
        return Arrays.stream(ErrorType.values())
                .filter(e -> e.getErrorType().equals(errorType))
                .findFirst()
                .orElseThrow(RuntimeException::new);
        // TODO: Exception customize
    }
}
