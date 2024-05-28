import java.util.ArrayList;

public class Epic extends Task{
    private ArrayList<Integer> subtaskId;


    public Epic() {}

    public Epic(String nameTask, String descriptionTask, Integer idTask, StatusTask status, ArrayList<Integer> subtaskId  ) {
        super(nameTask, descriptionTask, idTask, status);
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
        return "Epic{" +
                "subtaskId=" + subtaskId +
                ", nameTask='" + nameTask + '\'' +
                ", descriptionTask='" + descriptionTask + '\'' +
                ", idTask=" + idTask +
                ", status=" + status +
                '}';
    }
}
