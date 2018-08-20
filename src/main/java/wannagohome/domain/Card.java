package wannagohome.domain;

import java.util.Date;
import java.util.List;

public class Card {

    private User author;
    private String title;
    private String description;
    private List<Comment> comments;
    private List<User> assignees;
    private List<Label> labels;
    private Date createDate;
    private Date endDate;

    private boolean deleted;
    private Integer orderId;
}
