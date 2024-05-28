import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class TaskManager {
    public Integer id = 0;

    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, Subtask> subtasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();

    private Integer counterId() {
        id = id + 1;
        return id;
    }

    //удаление всех задач
    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteSubtasks() {
        for (Epic epic : epics.values()) {
            epic.getSubtaskId().clear();
            updateEpicStatus(epic);
        }
        subtasks.clear();
    }

    public void deleteEpics() {
        epics.clear();
    }


    //получение задачи по id
    public Task getTaskById(int taskId) {
        return tasks.get(taskId);
    }

    public Subtask getSubtaskById(int subtaskId) {
        return subtasks.get(subtaskId);
    }

    public Epic getEpicById(int epicId) {
        return epics.get(epicId);
    }


    // добавление задач
    public Integer addTask(Task task) {
        final int id = counterId();
        task.setIdTask(id);
        tasks.put(id, task);
        return id;
    }

    public Integer addEpic(Epic epic) {
        final int id = counterId();
        epic.setIdTask(id);
        updateEpicStatus(epic);
        epics.put(id, epic);
        return id;
    }

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
    public void updateTask(Task task) {
        final int id = task.getIdTask();
        final Task savedTask = tasks.get(id);
        if (savedTask == null) {
            return;
        }
        tasks.remove(id);
        tasks.put(id, task);
    }

    public void updateEpic(Epic epic) {
        final int id = epic.getIdTask();
        final Epic savedEpic = epics.get(id);
        if (savedEpic == null) {
            return;
        }
        epics.remove(id);
        epics.put(id, epic);
    }

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
    public void deleteTaskId(Integer id) {
        tasks.remove(id);
    }

    public void deleteEpicId(Integer id) {
        for (Integer subtaskId : epics.get(id).getSubtaskId()) {
            subtasks.remove(subtaskId);
        }
        epics.remove(id);
    }

    public void deleteSubtaskId(Integer id) {
        Epic epic = epics.get(subtasks.get(id).getEpicId());
        epic.getSubtaskId().remove(id);
        updateEpicStatus(epic);
        subtasks.remove(id);
    }


    //получение списка всех задач

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    //получение всех плдзадач из эпика

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


}
