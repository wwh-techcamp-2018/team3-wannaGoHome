package wannagohome.domain.user;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum UserPermission {
    ADMIN("Admin"),
    MANAGER("Manager"),
    MEMBER("Member");

    private String permission;

    public static UserPermission of(String permission) {
        return Arrays.stream(values()).filter(userPermission -> userPermission.permission.equals(permission)).findFirst().orElseThrow(() -> new IllegalArgumentException());
    }

    UserPermission(String permission) {
        this.permission = permission;
    }

    @JsonValue
    public String getPermission() {
        return permission;
    }
}
