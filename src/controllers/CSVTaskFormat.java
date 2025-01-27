package controllers;

import models.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CSVTaskFormat {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm");

    public static String toString(Task task) {
        String epicId = (task instanceof Subtask) ? String.valueOf(((Subtask) task).getEpicId()) : "";
        return String.format("%d,%s,%s,%s,%s,%s,%s,%s",
                task.getIdTask(),
                task.getType(),
                task.getNameTask(),
                task.getStatus(),
                task.getDescriptionTask().replace(",", " "),
                task.getStartTime() != null ? task.getStartTime().format(FORMATTER) : "",
                task.getDuration() != null ? task.getDuration().toString() : "",
                epicId
        );
    }

    public static Task fromString(String line) {
        System.out.println("Parsing line: " + line);

        String[] fields = line.split(",");
        if (fields.length < 7) {
            throw new IllegalArgumentException("Некорректный формат строки: " + line);
        }

        int id = Integer.parseInt(fields[0].trim());
        TypeOfTask type = TypeOfTask.valueOf(fields[1].trim());
        String name = fields[2].trim();
        StatusTask status = StatusTask.valueOf(fields[3].trim());
        String description = fields[4].trim();

        LocalDateTime startTime = fields[5].trim().isEmpty() ? null :
                LocalDateTime.parse(fields[5].trim(), FORMATTER);

        Duration duration = fields[6].trim().isEmpty() ? Duration.ZERO :
                Duration.parse(fields[6].trim());

        if (type == TypeOfTask.SUBTASK) {
            if (fields.length < 8) {
                throw new IllegalArgumentException("Отсутствует поле epicId для подзадачи: " + line);
            }
            int epicId = Integer.parseInt(fields[7].trim());
            return new Subtask(name, type, description, id, status, startTime, duration, epicId);
        } else if (type == TypeOfTask.EPIC) {
            return new Epic(name, type, description, id, status);
        } else {
            return new Task(name, type, description, id, status, startTime, duration);
        }
    }
}
