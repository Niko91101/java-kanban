package controllers;

import models.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    void remove(int id);

    public List<Task> getHistory();

}
