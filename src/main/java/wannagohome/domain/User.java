package wannagohome.domain;


import java.util.List;

public class User {

    private Long id;

    private String email;
    private String password;
    private String name;

    private List<Team> includedTeams;


    private boolean deleted;
}
