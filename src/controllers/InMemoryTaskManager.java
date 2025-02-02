package controllers;

import models.Epic;
import models.StatusTask;
import models.Subtask;
import models.Task;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

public class InMemoryTaskManager implements TaskManager {
    private Integer id = 0;


    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime,
            Comparator.nullsLast(Comparator.naturalOrder())));

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private Integer counterId() {
        id = id + 1;
        return id;
    }

    //удаление всех задач
    @Override
    public void deleteTasks() {
        tasks.values().forEach(task -> {
            prioritizedTasks.remove(task);
            historyManager.remove(task.getIdTask());
        });

        tasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        for (Epic epic : epics.values()) {
            epic.getSubtaskId().forEach(subtaskId -> {
                Subtask subtask = subtasks.get(subtaskId);
                if (subtask != null) {
                    prioritizedTasks.remove(subtask);
                    historyManager.remove(subtask.getIdTask());
                }
            });
            epic.getSubtaskId().clear();
            updateEpicStatus(epic);
            updateEpicTime(epic);
        }

        subtasks.clear();
    }

    @Override
    public void deleteEpics() {
        for (Epic epic : epics.values()) {
            epic.getSubtaskId().forEach(subtaskId -> {
                Subtask subtask = subtasks.get(subtaskId);
                if (subtask != null) {
                    prioritizedTasks.remove(subtask);
                    historyManager.remove(subtask.getIdTask());
                }
            });

            epics.clear();
            subtasks.clear();
        }
    }


    //получение задачи по id
    @Override
    public Task getTaskById(Integer taskId) {
        Task task = tasks.get(taskId);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Subtask getSubtaskById(Integer subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public Epic getEpicById(Integer epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }


    // добавление задач
    @Override
    public Integer addTask(Task task) {
        if (task.getStartTime() != null && hasTimeOverlap(task)) {
            System.out.println("Ошибка: задача пересекается с существующими задачами");
            return null;
        }


        int id = (task.getIdTask() != null) ? task.getIdTask() : counterId();
        task.setIdTask(id);

        tasks.put(id, task);
        prioritizedTasks.add(task);
        return id;
    }

    private boolean hasTimeOverlap(Task newTask) {
        return prioritizedTasks.stream()
                .filter(existingTask -> existingTask.getIdTask() != null)
                .filter(existingTask -> !existingTask.getIdTask().equals(newTask.getIdTask()))
                .anyMatch(existingTask -> isTimeOverlap(existingTask, newTask))
                || subtasks.values().stream()
                .filter(existingSubtask -> existingSubtask.getIdTask() != null)
                .filter(existingSubtask -> !existingSubtask.getIdTask().equals(newTask.getIdTask()))
                .anyMatch(existingSubtask -> isTimeOverlap(existingSubtask, newTask));
    }


    private boolean isTimeOverlap(Task task1, Task task2) {
        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = task1.getEndTime();
        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = task2.getEndTime();

        System.out.println("Сравниваем:");
        System.out.println("Задача 1: " + task1);
        System.out.println("Задача 2: " + task2);

        if (start1 == null || end1 == null || start2 == null || end2 == null) {
            System.out.println("Одна из задач не имеет времени, пересечения нет.");
            return false;
        }

        return start1.isBefore(end2) && end1.isAfter(start2);
    }

    @Override
    public Integer addEpic(Epic epic) {
        final int id = counterId();
        epic.setIdTask(id);
        updateEpicStatus(epic);
        epics.put(id, epic);
        return id;
    }

    @Override
    public Integer addSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            throw new IllegalArgumentException("Epic не найден! ID: " + subtask.getEpicId());
        }
        final int id = counterId();
        subtask.setIdTask(id);
        subtasks.put(id, subtask);
        epic.getSubtaskId().add(id);

        prioritizedTasks.add(subtask); //

        updateEpicStatus(epic);
        updateEpicTime(epic);
        return id;
    }


    //методы получения новой версии объекта
    @Override
    public void updateTask(Task task) {
        if (task.getStartTime() != null && hasTimeOverlap(task)) {
            System.out.println("Ошибка: задача пересекается с другими задачами.");
            return;
        }
        tasks.put(task.getIdTask(), task);
        prioritizedTasks.remove(task);
        prioritizedTasks.add(task);
    }

    @Override
    public void updateEpic(Epic epic) {
        final int id = epic.getIdTask();
        final Epic savedEpic = epics.get(id);
        if (savedEpic == null) {
            return;
        }
        epics.remove(id);
        epics.put(id, epic);
    }

    @Override
    public void updateSubtasks(Subtask subtask) {
        final int id = subtask.getIdTask();
        final Subtask savedSubtask = subtasks.get(id);
        if (savedSubtask == null) {
            return;
        }
        subtasks.put(id, subtask);
        Epic epic = epics.get(subtask.getEpicId());
        updateEpicStatus(epic);
        updateEpicTime(epic);
    }


    // методы удаления по id
    @Override
    public void deleteTaskId(Integer id) {
        Task task = tasks.remove(id);
        if (task != null) {
            prioritizedTasks.remove(task);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteEpicId(Integer id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubtaskId()) {
                subtasks.remove(subtaskId);
                prioritizedTasks.remove(subtasks.get(subtaskId));
            }
            epics.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteSubtaskId(Integer id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            prioritizedTasks.remove(subtask);
            historyManager.remove(id);
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.getSubtaskId().remove(id);
                updateEpicStatus(epic);
                updateEpicTime(epic);
            }
        }
    }
    //получение списка всех задач

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    //получение всех плдзадач из эпика

    @Override
    public List<Subtask> getSubtasksFromEpic(Epic epic) {
        return epic.getSubtaskId().stream()
                .map(subtasks::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        System.out.println("📌 Текущий список приоритетных задач:");
        prioritizedTasks.forEach(task -> System.out.println(" - " + task));
        return prioritizedTasks.stream()
                .filter(task -> task.getStartTime() != null)
                .collect(Collectors.toList());
    }

    // Обновление статуса эпика


    private void updateEpicStatus(Epic epic) {
        List<Subtask> subtasksList = epic.getSubtaskId().stream()
                .map(subtasks::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (subtasksList.isEmpty()) {
            epic.setStatus(StatusTask.NEW);
            return;
        }

        boolean allDone = subtasksList.stream().allMatch(subtask -> subtask.getStatus() == StatusTask.DONE);
        boolean allNew = subtasksList.stream().allMatch(subtask -> subtask.getStatus() == StatusTask.NEW);

        if (allDone) {
            epic.setStatus(StatusTask.DONE);
        } else if (allNew) {
            epic.setStatus(StatusTask.NEW);
        } else {
            epic.setStatus(StatusTask.IN_PROGRESS);
        }
    }

    private void updateEpicTime(Epic epic) {
        List<Subtask> subtasksList = epic.getSubtaskId().stream()
                .map(subtasks::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (subtasksList.isEmpty()) {
            epic.setStartTime(null);
            epic.setDuration(Duration.ZERO);
            epic.setEndTime(null);
            return;
        }

        epic.setStartTime(subtasksList.stream()
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null));

        epic.setEndTime(subtasksList.stream()
                .map(Subtask::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null));

        epic.setDuration(Duration.ofMinutes(
                subtasksList.stream()
                        .filter(s -> s.getDuration() != null)
                        .mapToLong(s -> s.getDuration().toMinutes())
                        .sum()
        ));
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}

