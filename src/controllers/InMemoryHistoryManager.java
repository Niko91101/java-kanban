package controllers;

import models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private Node first;
    private Node last;

    private final Map<Integer, Node> nodes = new HashMap<>();

    private void linkLast(Task task) {
        Node newNode = new Node(last, task, null);
        if (first == null) {
            first = newNode;
        } else {
            last.next = newNode;
        }
        last = newNode;
    }

    public List<Task> getTasks() {
        List<Task> history = new ArrayList<>();
        Node curNode = first;
        while (curNode != null) {
            history.add(curNode.task);
            curNode = curNode.next;
        }
        return history;
    }

    private void removeNode(int id) {
        Node node = nodes.remove(id);
        if (node == null) {
            return;
        }
        if (node.prev == null) {
            first = first.next;
            if (first == null) {
                last = null;
            } else {
                first.prev = null;
            }
        } else if (node.next == null) {
            last = last.prev;
            last.next = null;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
    }

    @Override
    public void add(Task task) {
        removeNode(task.getIdTask());
        linkLast(task);
        nodes.put(task.getIdTask(), last);
    }

    @Override
    public void remove(int id) {
        removeNode(id);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    public class Node {
        private Task task;
        private Node prev;
        private Node next;

        public Node(Node prev, Task task, Node next) {
            this.prev = prev;
            this.task = task;
            this.next = next;
        }
    }
}
