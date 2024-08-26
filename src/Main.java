
import controllers.Managers;
import controllers.TaskManager;
import models.*;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();


        Task task1 = new Task("Поиграть в футбол", TypeOfTask.TASK, "Завтра в 15,00", 0, StatusTask.NEW);
        int task1Id = manager.addTask(task1);


        Task task2 = new Task("Закончить это ТЗ", TypeOfTask.TASK, "Желательно завтра", 0, StatusTask.NEW);
        int task2Id = manager.addTask(task2);


        Epic epic1 = new Epic("Завершить этот год удачно", TypeOfTask.EPIC, StatusTask.NEW, "Планы на год", 0,
                new ArrayList<>());
        int epic1Id = manager.addEpic(epic1);


        Subtask subtask1 = new Subtask("Успешно сдать ТЗ № 4", TypeOfTask.SUBTASK, "Сейчас на верном пути", 0,
                StatusTask.IN_PROGRESS, epic1Id);


        Subtask subtask2 = new Subtask("Успешно сдать ТЗ № 5", TypeOfTask.SUBTASK, "В процессе", 0,
                StatusTask.DONE, epic1Id);


        System.out.println(manager.getEpicById(3));
        System.out.println(manager.getTaskById(2));
        System.out.println(manager.getSubtaskById(4));

        System.out.println(manager.getTaskById(1));
        System.out.println(manager.getEpicById(3));
        System.out.println(manager.getTaskById(1));

        System.out.println(manager.getSubtaskById(4));
        System.out.println(manager.getTaskById(1));
        System.out.println(manager.getTaskById(1));
        System.out.println(manager.getTaskById(1));
        System.out.println(manager.getTaskById(1));
        System.out.println(manager.getTaskById(1));


        printAllTasks(manager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getAllEpics()) {
            System.out.println(epic);

        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }
        System.out.println("История просмотров:");
        System.out.println(manager.getHistory());
    }
}


