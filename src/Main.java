import controllers.TaskManager;
import models.Epic;
import models.StatusTask;
import models.Subtask;
import models.Task;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Subtask subtask = new Subtask();

        Task task1 = new Task("Поиграть в футбол", "Завтра в 15,00", 0, StatusTask.NEW);
        int task1Id = taskManager.addTask(task1);

        Task task2 = new Task("Закончить это ТЗ", "Желательно завтра", 0, StatusTask.NEW);
        int task2Id = taskManager.addTask(task2);


        Epic epic1 = new Epic("Завершить этот год удачно", "Планы на год", 0,
                new ArrayList<>());
        int epic1Id = taskManager.addEpic(epic1);


        Subtask subtask1 = new Subtask("Успешно сдать ТЗ № 4", "Сейчас на верном пути", 0,
                StatusTask.IN_PROGRESS, epic1Id);
        int subtask1Id = taskManager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask("Успешно сдать ТЗ № 5", "Думаю там лютый пиздарез", 0,
                StatusTask.DONE, epic1Id);
        int Subtask2Id = taskManager.addSubtask(subtask2);


        // печать списка задач, эпиков и подзачад
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());


        // изменение статуса подзадачи и проверка изменения статуса эпика
        subtask1.setStatus(StatusTask.DONE);
        subtask2.setStatus(StatusTask.IN_PROGRESS);
        System.out.println(epic1.getStatus());

        //удаление одной из задач
        taskManager.deleteEpicId(epic1.getIdTask());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());

    }
}

