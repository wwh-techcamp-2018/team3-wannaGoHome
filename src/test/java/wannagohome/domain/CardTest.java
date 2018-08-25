package wannagohome.domain;

import org.junit.Before;
import org.junit.Test;
import wannagohome.exception.BadRequestException;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;


public class CardTest {

    private Card card;

    private User assignee;

    private User newAssignee;

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
                .build();

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
}