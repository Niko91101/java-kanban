package models;

public class Subtask extends Task {
    private Integer epicId;


    public Subtask(String nameTask, TypeOfTask type, String descriptionTask, Integer idTask, StatusTask status, Integer epicId) {
        super(nameTask, type, descriptionTask, idTask, status);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "models.Subtask{" +
                "epicId=" + epicId +
                ", type=" + getType() +
                ", nameTask='" + getNameTask() + '\'' +
                ", descriptionTask='" + getDescriptionTask() + '\'' +
                ", idTask=" + getIdTask() +
                ", status=" + getStatus() +
                '}';
    }
}
