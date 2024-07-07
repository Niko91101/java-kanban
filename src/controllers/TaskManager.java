package controllers;

import models.Epic;
import models.Subtask;
import models.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    List<Task> getHistory();

    //удаление всех задач
    void deleteTasks();

    void deleteSubtasks();

    void deleteEpics();

    //получение задачи по id
    Task getTaskById(Integer taskId);

    Subtask getSubtaskById(Integer subtaskId);

    Epic getEpicById(Integer epicId);

    // добавление задач
    Integer addTask(Task task);

    Integer addEpic(Epic epic);

    Integer addSubtask(Subtask subtask);

    //методы получения новой версии объекта
    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtasks(Subtask subtask);

    // методы удаления по id
    void deleteTaskId(Integer id);

    void deleteEpicId(Integer id);

    void deleteSubtaskId(Integer id);

    ArrayList<Task> getAllTasks();

    ArrayList<Epic> getAllEpics();

    ArrayList<Subtask> getAllSubtasks();

    ArrayList<Subtask> getSubtasksFromEpic(Epic epic);



}
