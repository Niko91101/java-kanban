
import controllers.Managers;
import controllers.TaskManager;
import models.*;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();

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


