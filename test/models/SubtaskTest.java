package models;

import models.Subtask;
import models.StatusTask;
import models.TypeOfTask;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class SubtaskTest {

    @Test
    public void testEpicIdAssignment() {
        Subtask subtask = new Subtask("Subtask", TypeOfTask.SUBTASK, "Description", 2,
                StatusTask.NEW, LocalDateTime.of(2023, 1, 2, 10, 0),
                Duration.ofHours(1), 1);
        assertEquals(1, subtask.getEpicId());
    }
}
