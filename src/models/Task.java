package models;

public class Task {

    private String nameTask;
    private String descriptionTask;
    private Integer idTask;
    private StatusTask status;

    public Task() {}

    public Task(String nameTask, String descriptionTask, Integer idTask, StatusTask status) {
        this.nameTask = nameTask;
        this.descriptionTask = descriptionTask;
        this.idTask = idTask;
        this.status = status;
    }

    public Task(String nameTask, String descriptionTask, Integer idTask) {
        this.nameTask = nameTask;
        this.descriptionTask = descriptionTask;
        this.idTask = idTask;
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

    public int getIdTask() {
        return idTask;
    }

    public void setIdTask(int idTask) {
        this.idTask = idTask;
    }

    @Override
    public String toString() {
        return "models.Task{" +
                "nameTask='" + nameTask + '\'' +
                ", descriptionTask='" + descriptionTask + '\'' +
                ", idTask=" + idTask +
                ", status=" + status +
                '}';
    }
}
