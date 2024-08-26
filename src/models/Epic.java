package models;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskId;


    public Epic(String nameTask, TypeOfTask type, StatusTask statusTask, String descriptionTask, Integer idTask, ArrayList<Integer> subtaskId) {
        super(nameTask, type, descriptionTask, idTask, statusTask);
        this.subtaskId = subtaskId;
    }

    public ArrayList<Integer> getSubtaskId() {
        return subtaskId;
    }

    public void setSubtaskId(ArrayList<Integer> subtaskId) {
        this.subtaskId = subtaskId;
    }


    @Override
    public String toString() {
        return "models.Epic{" +
                "subtaskId=" + subtaskId +
                ", nameTask='" + getNameTask() + '\'' +
                ", descriptionTask='" + getDescriptionTask() + '\'' +
                ", idTask=" + getIdTask() +
                ", status=" + getIdTask() +
                '}';
    }
}
