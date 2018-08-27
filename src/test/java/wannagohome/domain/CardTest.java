package wannagohome.domain;

import org.junit.Before;
import org.junit.Test;
import wannagohome.domain.card.Card;
import wannagohome.domain.card.Label;
import wannagohome.domain.card.LabelColor;
import wannagohome.domain.user.User;
import wannagohome.exception.BadRequestException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class CardTest {

    private Card card;

    private User assignee;

    private User newAssignee;

    private Label label;

    private Label existLabel;

    @Before
    public void setUp() throws Exception {
        assignee = User.builder()
                .id(1L)
                .email("junsulime@junsulime.com")
                .password("password11")
                .name("junsulime")
                .build();

        newAssignee = User.builder()
                .id(2L)
                .email("yeon@yeon.com")
                .password("passport11")
                .name("kim")
                .build();

        card = Card.builder()
                .id(1L)
                .assignees(new ArrayList<>())
                .description("hello")
                .labels(new ArrayList<>())
                .build();

        label = Label.builder()
                .id(1L)
                .color(LabelColor.RED)
                .build();

        existLabel = Label.builder()
                .id(2L)
                .color(LabelColor.ORANGE)
                .build();
        List<Label> labelList = new ArrayList<>();
        labelList.add(existLabel);
        card.setLabels(labelList);
        card.addAssignee(assignee);
    }

    @Test
    public void addAssignee() {
        card.addAssignee(newAssignee);
        assertThat(card.getAssignees()).contains(newAssignee);
    }

    @Test(expected = BadRequestException.class)
    public void addAssignee_assignee이미_존재() {
        card.addAssignee(assignee);
    }

    @Test
    public void dischargeAssignee() {
        card.dischargeAssignee(assignee);
        assertThat(card.getAssignees()).doesNotContain(assignee);
    }

    @Test(expected = BadRequestException.class)
    public void dischargeAssignee_지울_assignee가_없음() {
        card.dischargeAssignee(newAssignee);
    }

    @Test(expected = BadRequestException.class)
    public void removeLabel_지울라벨없을때() {
        card.removeLabel(label);
    }

    @Test
    public void removeLabel() {
        card.removeLabel(existLabel);
        assertThat(card.getLabels()).doesNotContain(existLabel);
    }

}