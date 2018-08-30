package wannagohome.domain.error;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum ErrorType {
    UNAUTHORIZED("unauthorized"),
    UNAUTHENTICATED("unauthenticated"),
    USER_ID("userId"),
    USER_EMAIL("email"),
    USER_NAME("name"),
    USER_PASSWORD("password"),
    TEAM_NAME("name"),
    TEAM_DESC("description"),
    BOARD_ID("boardId"),
    BOARD_TITLE("title"),
    CARD_ID("cardId"),
    EMPTY_FILE("emptyFile"),
    UNSUPPORTED_FILE("unsupportedFile"),
    EXCEED_FILE("exceedFile"),
    CARD_ASSIGN_ALREADY_EXIST("cardAssigneeAlreadyExist"),
    CARD_ASSIGN_NOT_EXIST("cardAssigneeNotExist"),
    CARD_LABEL_NOT_EXIST("cardLabelNotExist"),
    COMMENT_ID("commendId"),
    LABEL_ID("labelId"),
    CARD_DUE_DATE_NOT_EXIST("cardDueDateNotExist"),
    FILE_ID("fileId"),
    TEAM_INVITE_ID("teamInviteId"),
    TASK_ID("taskId");

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
