package wannagohome.domain.activity;

public enum ActivityType {

    BOARD_CREATE("Board.Create"),
    BOARD_UPDATE("Board.Update"),
    BOARD_DELETE("Board.Delete"),
    BOARD_MEMBER_ADD("Board.Member.Add"),

    TEAM_DELETE("Team.Delete"),
    TEAM_AUTHORITY("Team.Authority"),
    TEAM_MEMBER_ADD("Team.Member.Add"),
    TEAM_MEMBER_INVITE("Team.Member.Invite"),
    TEAM_MEMBER_REMOVE("Team.Member.Remove"),

    TASK_CREATE("Task.Create"),
    TASK_UPDATE("Task.Update"),
    TASK_DELETE("Task.Delete"),

    CARD_CREATE("Card.Create"),
    CARD_UPDATE_DUE_DATE("Card.Update.DueDate"),
    CARD_UPDATE_DESCRIPTION("Card.Update.Description"),
    CARD_UPDATE_COMMENT("Card.Update.Comment"),
    CARD_UPDATE_LABEL("Card.Update.Label"),
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
