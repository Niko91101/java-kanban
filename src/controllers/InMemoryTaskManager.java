package controllers;

import models.Epic;
import models.StatusTask;
import models.Subtask;
import models.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private Integer id = 0;


    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private Integer counterId() {
        id = id + 1;
        return id;
    }

    //удаление всех задач
    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        for (Epic epic : epics.values()) {
            epic.getSubtaskId().clear();
            updateEpicStatus(epic);
        }
        subtasks.clear();
    }

    @Override
    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }


    //получение задачи по id
    @Override
    public Task getTaskById(Integer taskId) {
        historyManager.add(tasks.get(taskId));
        return tasks.get(taskId);
    }

    @Override
    public Subtask getSubtaskById(Integer subtaskId) {
        historyManager.add(subtasks.get(subtaskId));
        return subtasks.get(subtaskId);
    }

    @Override
    public Epic getEpicById(Integer epicId) {
        historyManager.add(epics.get(epicId));
        return epics.get(epicId);
    }


    // добавление задач
    @Override
    public Integer addTask(Task task) {
        final int id = counterId();
        task.setIdTask(id);
        tasks.put(id, task);
        return id;
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
            return null;
        }
        final int id = counterId();
        subtask.setIdTask(id);
        subtasks.put(id, subtask);
        epic.getSubtaskId().add(id);
        updateEpicStatus(epic);
        return id;
    }


    //методы получения новой версии объекта
    @Override
    public void updateTask(Task task) {
        final int id = task.getIdTask();
        final Task savedTask = tasks.get(id);
        if (savedTask == null) {
            return;
        }
        tasks.remove(id);
        tasks.put(id, task);
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
        final int id = subtask.getEpicId();
        final Task savedSubtask = subtasks.get(id);
        if (savedSubtask == null) {
            return;
        }
        subtasks.remove(id);
        subtasks.put(id, subtask);
        Epic epic = epics.get(subtask.getEpicId());
        updateEpic(epic);
    }


    // методы удаления по id
    @Override
    public void deleteTaskId(Integer id) {
        tasks.remove(id);
    }

    @Override
    public void deleteEpicId(Integer id) {
        for (Integer subtaskId : epics.get(id).getSubtaskId()) {
            subtasks.remove(subtaskId);
        }
        epics.remove(id);
    }

    @Override
    public void deleteSubtaskId(Integer id) {
        Epic epic = epics.get(subtasks.get(id).getEpicId());
        epic.getSubtaskId().remove(id);
        updateEpicStatus(epic);
        subtasks.remove(id);
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
    public ArrayList<Subtask> getSubtasksFromEpic(Epic epic) {
        ArrayList<Subtask> subtasksFromEpic = new ArrayList<>();
        for (Integer subtaskId : epic.getSubtaskId()) {
            subtasksFromEpic.add(subtasks.get(subtaskId));
        }
        return subtasksFromEpic;
    }

    // Обновление статуса эпика


    private void updateEpicStatus(Epic epic) {
        if (epic.getSubtaskId().equals(Collections.emptyList())) {
            epic.setStatus(StatusTask.NEW);
            return;
        }
        boolean isEpisStatusNew = false;
        boolean isEpicStatusInProgress = false;
        boolean isEpicStatusDone = false;

        for (Integer subtaskId : epic.getSubtaskId()) {
            if (subtasks.get(subtaskId).getStatus() == StatusTask.NEW) {
                isEpisStatusNew = true;
            }
            if (subtasks.get(subtaskId).getStatus() == StatusTask.IN_PROGRESS) {
                isEpicStatusInProgress = true;
            }
            if (subtasks.get(subtaskId).getStatus() == StatusTask.DONE) {
                isEpicStatusDone = true;
            }
        }

        if (isEpisStatusNew && !isEpicStatusInProgress && !isEpicStatusDone) {
            epic.setStatus(StatusTask.NEW);
        } else if (isEpicStatusDone && !isEpisStatusNew && !isEpicStatusInProgress) {
            epic.setStatus(StatusTask.DONE);
        } else {
            epic.setStatus(StatusTask.IN_PROGRESS);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}

