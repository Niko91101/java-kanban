package controllers;

import models.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    static File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) {
        TaskManager taskManager = new FileBackedTaskManager(new File("/Users/user/Desktop/test7/newFile.csv"));

        System.out.println("Создаем задачи, эпики и подзадачи ");
        Task task1 = new Task(
                "Поиграть в футбол", TypeOfTask.TASK, "Завтра в 15,00", 0, StatusTask.NEW);
        taskManager.addTask(task1);

        Task task2 = new Task(
                "Закончить это ТЗ", TypeOfTask.TASK, "Желательно завтра", 0, StatusTask.NEW);
        taskManager.addTask(task2);

        Epic epic1 = new Epic(
                "Завершить этот год удачно", TypeOfTask.EPIC, StatusTask.NEW, "Планы на год", 0,
                new ArrayList<>());
        int epic1Id = taskManager.addEpic(epic1);

        Subtask subtask1 = new Subtask(
                "Успешно сдать ТЗ № 4", TypeOfTask.SUBTASK, "Сейчас на верном пути", 0,
                StatusTask.IN_PROGRESS, epic1Id);
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask(
                "Успешно сдать ТЗ № 5", TypeOfTask.SUBTASK, "В процессе", 0,
                StatusTask.DONE, epic1Id);
        taskManager.addSubtask(subtask2);

        System.out.println("Проверяем что все что было в старом менеджере, есть в новом \n");

        try {
            System.out.println(Files.readString(Path.of("/Users/user/Desktop/test7/newFile.csv")));
        } catch (IOException exception) {
            System.out.println("sdasdasd");
        }

        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(
                new File("/Users/user/Desktop/test7/newFile.csv"));

        System.out.println("Таски: ");
        for (Task task : fileBackedTaskManager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("Сабтаски: ");
        for (Task subtask : fileBackedTaskManager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("Эпики: ");
        for (Task epic : fileBackedTaskManager.getAllEpics()) {
            System.out.println(epic);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        if (file.length() == 0) {
            throw new ManagerException("Файл пустой");
        } else if (file.isDirectory()) {
            throw new ManagerException("Вместо файла указана директория");
        } else if (!(file.exists())) {
            try {
                Files.createFile(file.toPath());
            } catch (IOException exception) {
                throw new RuntimeException("Файл не найден, ошибка при создании");
            }
        }

        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            while (fileReader.ready()) {
                String line = fileReader.readLine();
                if (line.equals("id,type,name,status,description,epic")) {
                    continue;
                }
                Task task = CSVTaskFormat.taskFromString(line);

                switch (task.getType()) {
                    case TASK -> fileBackedTaskManager.addTask(task);
                    case EPIC -> {
                        if (task instanceof Epic) {
                            fileBackedTaskManager.addEpic((Epic) task);
                        }
                    }
                    case SUBTASK -> {
                        if (task instanceof Subtask) {
                            fileBackedTaskManager.addSubtask((Subtask) task);
                        }
                    }
                    default -> throw new ManagerException("Неизвестный тип");
                }
            }
        } catch (IOException exception) {
            throw new ManagerException(exception.getMessage());
        }
        return fileBackedTaskManager;
    }

    public void save() {
        List<Task> allTask = new ArrayList<>();
        allTask.addAll(getAllTasks());
        allTask.addAll(getAllEpics());
        allTask.addAll(getAllSubtasks());

        try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8)) {
            fileWriter.write("id,type,name,status,description,epic" + "\n");
            for (Task task : allTask) {
                fileWriter.write(CSVTaskFormat.toString(task) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerException("Произошла ошибка во время записи в файл");
        }
    }

    //Добавление
    @Override
    public Integer addTask(Task task) {
        Integer newTaskId = super.addTask(task);
        save();
        return newTaskId;
    }

    @Override
    public Integer addEpic(Epic epic) {
        Integer newEpicId = super.addEpic(epic);
        save();
        return newEpicId;
    }

    @Override
    public Integer addSubtask(Subtask subtask) {
        Integer newSubtaskId = super.addSubtask(subtask);
        save();
        return newSubtaskId;
    }

    //удаление всех задач
    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    // методы удаления по id
    @Override
    public void deleteTaskId(Integer id) {
        super.deleteTaskId(id);
        save();
    }

    @Override
    public void deleteEpicId(Integer id) {
        super.deleteEpicId(id);
        save();
    }

    @Override
    public void deleteSubtaskId(Integer id) {
        super.deleteSubtaskId(id);
        save();
    }

    //методы получения новой версии объекта
    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtasks(Subtask subtask) {
        super.updateSubtasks(subtask);
        save();
    }

}
