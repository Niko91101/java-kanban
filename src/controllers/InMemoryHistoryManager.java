package controllers;

import models.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final ArrayList<Task> historyWatching = new ArrayList<>();
    private final static int SIZE = 10;
    @Override
    public void add(Task task) {
        if(task != null) {
            if(historyWatching.size() == SIZE) {
                historyWatching.remove(historyWatching.get(0));
            }
            historyWatching.add(task);
        }
    }
    @Override
    public List<Task> getHistory() {
        return historyWatching;
    }

}
