package wannagohome.domain.user;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserPermission {
    ADMIN("Admin"),
    MANAGER("Manager"),
    MEMBER("Member");

    private String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    @JsonValue
    public String getPermission() {
        return permission;
    }
}
