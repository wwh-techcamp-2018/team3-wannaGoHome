package wannagohome.domain;

public enum ActivityType {

    BOARD_CREATE("Board.Create"),
    BOARD_UPDATE("Board.Update"),
    BOARD_DELETE("Board.Delete"),
    BOARD_MEMBER_ADD("Board.Member.Add"),

    TEAM_AUTHORITY("Team.Authority"),
    TEAM_MEMBER_ADD("Team.Member.Add"),

    TASK_CREATE("Task.Create"),
    TASK_UPDATE("Task.Update"),
    TASK_DELETE("Task.Delete"),

    CARD_CREATE("Card.Create"),
    CARD_UPDATE("Card.Update"),
    CARD_DELETE("Card.Delete"),
    CARD_ASSIGN("Card.Assign");

    private String code;

    ActivityType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
