package wannagohome.domain;

import java.util.List;

public class Team {

    private String profileImage;
    private String name;
    private String description;

    private List<User> members;

    private List<Board> boards;

    private boolean deleted;
}
