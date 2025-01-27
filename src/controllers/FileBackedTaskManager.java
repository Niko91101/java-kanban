package controllers;

import models.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
public class FileBackedTaskManager extends InMemoryTaskManager {

    private static File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) {
        TaskManager manager = new FileBackedTaskManager(new File("src/resources/file.csv"));

        System.out.println("Создаем задачи, эпики и подзадачи ");
        manager.addTask(new Task("Task 1", TypeOfTask.TASK, "Description 1", 1, StatusTask.NEW,
                LocalDateTime.of(2023, 1, 1, 10, 0), Duration.ofHours(1)));
        manager.addTask(new Task("Task 2", TypeOfTask.TASK, "Description 2", 2, StatusTask.NEW,
                LocalDateTime.of(2023, 1, 1, 8, 0), Duration.ofHours(1)));
//
        Epic epic1 = new Epic("Epic Task", TypeOfTask.EPIC, "Epic Description", 3, StatusTask.NEW);
        manager.addEpic(epic1);

        Subtask subtask1 = new Subtask("Subtask 1", TypeOfTask.SUBTASK, "Subtask Description 1", 4,
                StatusTask.NEW, LocalDateTime.of(2025, 1, 20, 12, 0), Duration.ofHours(1), epic1.getIdTask());
        Subtask subtask2 = new Subtask("Subtask 2", TypeOfTask.SUBTASK, "Subtask Description 2", 5,
                StatusTask.IN_PROGRESS, LocalDateTime.of(2025, 1, 20, 15, 0), Duration.ofHours(2), epic1.getIdTask());


        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);



        System.out.println("Проверяем что все что было в старом менеджере, есть в новом \n");

        try {
            System.out.println(Files.readString(Path.of("src/resources/file.csv")));
        } catch (IOException exception) {
            System.out.println("sdasdasd");
        }

        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(
                new File("src/resources/file.csv"));

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

        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            fileReader.readLine();
            while (fileReader.ready()) {
                String line = fileReader.readLine();
                Task task = CSVTaskFormat.fromString(line);
                switch (task.getType()) {
                    case TASK -> manager.addTask(task);
                    case EPIC -> manager.addEpic((Epic) task);
                    case SUBTASK -> manager.addSubtask((Subtask) task);
                }
            }
        } catch (IOException exception) {
            throw new ManagerException("Ошибка при чтении файла: " + exception.getMessage());
        }
        return manager;
    }

    public void save() {
        try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8)) {
            fileWriter.write("id,type,name,status,description,startTime,duration,epic \n");
            for (Task task : getAllTasks()) {
                fileWriter.write(CSVTaskFormat.toString(task) + "\n");
            }
            for (Epic epic : getAllEpics()) {
                fileWriter.write(CSVTaskFormat.toString(epic) + "\n");
            }
            for (Subtask subtask : getAllSubtasks()) {
                fileWriter.write(CSVTaskFormat.toString(subtask) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerException("Ошибка при сохранении файла");
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
