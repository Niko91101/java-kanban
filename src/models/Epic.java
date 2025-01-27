package models;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class Epic extends Task {

    private ArrayList<Integer> subtaskId;
    private LocalDateTime endTime;


    public Epic(String nameTask, TypeOfTask type, String descriptionTask, Integer idTask, StatusTask status) {
        super(nameTask, type, descriptionTask, idTask, status);
    }

    public ArrayList<Integer> getSubtaskId() {
        if (subtaskId == null) {
            subtaskId = new ArrayList<>();
        }
        return subtaskId;
    }


    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setSubtaskId(ArrayList<Integer> subtaskId) {
        this.subtaskId = subtaskId;
    }


    public LocalDateTime getEndTime() {
        return endTime;
    }


    @Override
    public String toString() {
        return "Epic{" +
                "subtaskIds=" + subtaskId +
                ", name='" + getNameTask() + '\'' +
                ", description='" + getDescriptionTask() + '\'' +
                ", id=" + getIdTask() +
                ", status=" + getStatus() +
                ", startTime=" + getStartTime() +
                ", duration=" + getDuration() +
                ", endTime=" + endTime +
                '}';
    }
}
