package models;
import models.Task;
import models.StatusTask;
import models.TypeOfTask;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    public void testGetEndTime() {
        Task task = new Task("Test Task", TypeOfTask.TASK, "Test Description", 1, StatusTask.NEW,
                LocalDateTime.of(2023, 1, 1, 10, 0), Duration.ofHours(2));
        assertEquals(LocalDateTime.of(2023, 1, 1, 12, 0), task.getEndTime());
    }

    @Test
    public void testEndTimeWithNullStartTime() {
        Task task = new Task("Test Task", TypeOfTask.TASK, "Test Description", 1, StatusTask.NEW);
        assertNull(task.getEndTime());
    }

    @Test
    public void shouldCalculateEndTimeCorrectly() {
        Task task = new Task("Test Task", TypeOfTask.TASK, "Description", 1, StatusTask.NEW,
                LocalDateTime.of(2024, 1, 1, 10, 0), Duration.ofHours(2));
        assertEquals(LocalDateTime.of(2024, 1, 1, 12, 0), task.getEndTime());
    }

    @Test
    public void shouldReturnNullWhenStartTimeIsNull() {
        Task task = new Task("Test Task", TypeOfTask.TASK, "Description", 1, StatusTask.NEW);
        assertNull(task.getEndTime());
    }

    @Test
    public void shouldAllowUpdatingStartTimeAndDuration() {
        Task task = new Task("Test Task", TypeOfTask.TASK, "Description", 1, StatusTask.NEW);
        task.updateTimeParameters(LocalDateTime.of(2024, 1, 1, 14, 0), Duration.ofHours(3));
        assertEquals(LocalDateTime.of(2024, 1, 1, 14, 0), task.getStartTime());
        assertEquals(Duration.ofHours(3), task.getDuration());
    }
}
