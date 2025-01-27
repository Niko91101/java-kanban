package models;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private Integer epicId;


    public Subtask(String nameTask, TypeOfTask type, String descriptionTask, Integer idTask, StatusTask status, LocalDateTime startTime, Duration duration, Integer epicId) {
        super(nameTask, type, descriptionTask, idTask, status, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(String nameTask, TypeOfTask type, String descr, Integer id, StatusTask statusTask) {
        super(nameTask, type, descr, id, statusTask);
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                ", name='" + getNameTask() + '\'' +
                ", description='" + getDescriptionTask() + '\'' +
                ", id=" + getIdTask() +
                ", status=" + getStatus() +
                ", startTime=" + getStartTime() +
                ", duration=" + getDuration() +
                '}';
    }
}
