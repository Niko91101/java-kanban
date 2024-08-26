package controllers;

import models.*;

import java.util.ArrayList;

public class CSVTaskFormat {

    public static String toString(Task task) {
        String taskToString;
        taskToString = String.format("%s,%s,%s,%s,%s",
                task.getIdTask(),
                task.getType(),
                task.getNameTask(),
                task.getStatus(),
                task.getDescriptionTask()
        );
        if (task.getType() == TypeOfTask.SUBTASK) {
            taskToString = taskToString + String.format(",%s", ((Subtask) task).getEpicId());
        }
        return taskToString;
    }

    public static Task taskFromString(String value) {
        final String[] values = value.split(",");

        final Integer id = Integer.parseInt(values[0]);
        final TypeOfTask type = TypeOfTask.valueOf(values[1]);
        final String name = values[2];
        final StatusTask status = StatusTask.valueOf(values[3]);
        final String description = values[4];

        if (type == TypeOfTask.TASK) {
            return new Task(name, type, description, id, status);
            //(String nameTask, TypeOfTask type, String descriptionTask, Integer idTask, StatusTask status)
        } else if (type == TypeOfTask.EPIC) {
            return new Epic(name, type, status, description, id, new ArrayList<>());
            //String nameTask, TypeOfTask type,  StatusTask statusTask, String descriptionTask, Integer idTask, ArrayList<Integer> subtaskId
        } else if (type == TypeOfTask.SUBTASK) {
            final Integer epicId = Integer.parseInt(values[5]);
            return new Subtask(name, type, description, id, status, epicId);
            //String nameTask, TypeOfTask type, String descriptionTask, Integer idTask, StatusTask status, Integer epicId
        }
        return null;
    }


}
