package wannagohome.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TaskTest {
    private Task task;
    private Card card1;
    private Card card2;
    private Card card3;

    @Before
    public void setUp() throws Exception {
        task = new Task();
        card1 = card1.builder()
                .title("card1 title")
                .description("desc")
                .id(1L)
                .build();
        card2 = card2.builder()
                .title("card2 title")
                .description("desc")
                .id(2L)
                .build();
        card3 = card3.builder()
                .title("card3 title")
                .description("desc")
                .id(3L)
                .build();

        task.setCards(new ArrayList<>(Arrays.asList(card2, card3)));
    }

    @Test
    public void addCard() {
        task.addCard(card1);
        assertThat(task.getCards().size(), is(3));
    }

}
