package models;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    private String nameTask;
    private String descriptionTask;
    private Integer idTask;
    private StatusTask status;
    private TypeOfTask type;
    private Duration duration = Duration.ZERO;
    private LocalDateTime startTime;


    public Task(String nameTask, TypeOfTask type, String descriptionTask, Integer idTask, StatusTask status) {
        this.nameTask = nameTask;
        this.type = type;
        this.descriptionTask = descriptionTask;
        this.idTask = idTask;
        this.status = status;
    }

    public Task(String nameTask, TypeOfTask type, String descriptionTask, Integer idTask, StatusTask status, LocalDateTime startTime, Duration duration) {
        this.nameTask = nameTask;
        this.type = type;
        this.descriptionTask = descriptionTask;
        this.idTask = idTask;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }


    public String getNameTask() {
        return nameTask;
    }

    public StatusTask getStatus() {
        return status;
    }

    public void setStatus(StatusTask status) {
        this.status = status;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public String getDescriptionTask() {
        return descriptionTask;
    }

    public void setDescriptionTask(String descriptionTask) {
        this.descriptionTask = descriptionTask;
    }

    public Integer getIdTask() {
        return idTask;
    }

    public void setIdTask(int idTask) {
        this.idTask = idTask;
    }

    public TypeOfTask getType() {
        return type;
    }

    public void setType(TypeOfTask type) {
        this.type = type;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        return (startTime != null && duration != null) ? startTime.plus(duration) : null;
    }

    public void updateTimeParameters(LocalDateTime startTime, Duration duration) {
        this.startTime = startTime;
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return Objects.equals(idTask, task.idTask) &&
                Objects.equals(nameTask, task.nameTask) &&
                Objects.equals(descriptionTask, task.descriptionTask) &&
                status == task.status &&
                type == task.type &&
                Objects.equals(startTime, task.startTime) &&
                Objects.equals(duration, task.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameTask, descriptionTask, idTask, status, type, startTime, duration);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + nameTask + '\'' +
                ", description='" + descriptionTask + '\'' +
                ", id=" + idTask +
                ", status=" + status +
                ", type=" + type +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }
}