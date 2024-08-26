package controllers;

import models.Epic;
import models.StatusTask;
import models.Subtask;
import models.TypeOfTask;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicStatusTest {

    TaskManager manager = Managers.getDefault();

    Epic testEpic = new Epic(
            "Тест", TypeOfTask.EPIC, StatusTask.NEW, "Планы на год", 0,
            new ArrayList<>());
    int testEpicId = manager.addEpic(testEpic);

    @Test
    public void statusEpicEmpty() {
        assertEquals(StatusTask.NEW, testEpic.getStatus(), "Статусы отличаются.");
    }

    @Test
    public void statusEpicNewNew() {
        Subtask testSubtask1 = new Subtask(
                "СабтаскТест1", TypeOfTask.SUBTASK, "Сейчас на верном пути", 0,
                StatusTask.NEW, testEpicId);
        int testSubtask1Id = manager.addSubtask(testSubtask1);

        Subtask testSubtask2 = new Subtask("СабтаскТест1", TypeOfTask.SUBTASK, "Сейчас на верном пути", 0,
                StatusTask.NEW, testEpicId);
        manager.addSubtask(testSubtask1);
        manager.addSubtask(testSubtask2);

        assertEquals(StatusTask.NEW, testEpic.getStatus(), "Статусы отличаются.");
    }

    @Test
    public void statusEpicNewDone() {
        Subtask testSubtask1 = new Subtask(
                "СабтаскТест1", TypeOfTask.SUBTASK, "Сейчас на верном пути", 0,
                StatusTask.IN_PROGRESS, testEpicId);
        int testSubtask1Id = manager.addSubtask(testSubtask1);

        Subtask testSubtask2 = new Subtask("СабтаскТест1", TypeOfTask.SUBTASK, "Сейчас на верном пути", 0,
                StatusTask.IN_PROGRESS, testEpicId);
        manager.addSubtask(testSubtask1);
        manager.addSubtask(testSubtask2);

        assertEquals(StatusTask.IN_PROGRESS, testEpic.getStatus(), "Статусы отличаются.");
    }


    @Test
    public void statusEpicProgressProgress() {
        Subtask testSubtask1 = new Subtask(
                "СабтаскТест1", TypeOfTask.SUBTASK, "Сейчас на верном пути", 0,
                StatusTask.IN_PROGRESS, testEpicId);
        int testSubtask1Id = manager.addSubtask(testSubtask1);

        Subtask testSubtask2 = new Subtask("СабтаскТест1", TypeOfTask.SUBTASK, "Сейчас на верном пути", 0,
                StatusTask.IN_PROGRESS, testEpicId);
        manager.addSubtask(testSubtask1);
        manager.addSubtask(testSubtask2);

        assertEquals(StatusTask.IN_PROGRESS, testEpic.getStatus(), "Статусы отличаются.");
    }


}
