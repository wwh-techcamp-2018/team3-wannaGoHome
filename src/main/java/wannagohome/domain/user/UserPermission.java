package wannagohome.domain.user;

public enum UserPermission {
    ADMIN(0),
    MANAGER(1),
    MEMBER(2);

    private int permission;

    UserPermission(int permission) {
        this.permission = permission;
    }
}
