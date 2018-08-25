package wannagohome.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BoardTest {
    private Board testBoard;
    private Task dummyTask;

    @Before
    public void setUp() throws Exception {
        testBoard = new Board();

        testBoard.setTasks(new ArrayList<Task>(Arrays.asList(
                new Task(1L, User.GUEST_USER, testBoard, "first task", null, false, 0),
                new Task(2L, User.GUEST_USER, testBoard, "second task", null, false, 1)
        )));

        dummyTask = new Task(3L, User.GUEST_USER, testBoard, "dummy task", null, false, 2);
    }

    @Test
    public void addTask_성공() {
        testBoard.addTask(dummyTask);
        assertThat(testBoard.getTasks().size(), is(3));
    }

    @Test
    public void reorderTask_성공() {
        TaskOrderDto reorderDto = new TaskOrderDto(2L, 0);
        testBoard.reorderTasks(reorderDto);
        assertThat(testBoard.getTasks().get(0).getId(), is(2L));
    }

}