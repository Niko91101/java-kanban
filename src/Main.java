import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task = new Task();
        Epic epic = new Epic();
        Subtask subtask = new Subtask();

        Task task1 = new Task("Поиграть в футбол", "Завтра в 15,00", 0, StatusTask.NEW);
        int task1Id = taskManager.addTask(task1);

        Task task2 = new Task("Закончить это ТЗ", "Желательно завтра", 0, StatusTask.NEW);
        int task2Id = taskManager.addTask(task2);


        Epic epic1 = new Epic("Завершить этот год удачно", "Планы на год", 0, StatusTask.NEW,
                new ArrayList<>());
        int epic1Id = taskManager.addEpic(epic1);


        Subtask subtask1 = new Subtask("Успешно сдать ТЗ № 4", "Сейчас на верном пути", 0,
                StatusTask.IN_PROGRESS, epic1Id);
        int subtask1Id = taskManager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask("Успешно сдать ТЗ № 5", "Думаю там лютый пиздарез", 0,
                StatusTask.NEW, epic1Id);
        int Subtask2Id = taskManager.addSubtask(subtask2);



        System.out.println(taskManager.tasks);
        taskManager.deleteTasks();
        System.out.println(taskManager.tasks);

        System.out.println(taskManager.epics);
        taskManager.deleteEpics();
        System.out.println(taskManager.epics);

        System.out.println(taskManager.subtasks);
        taskManager.deleteSubtasks();
        System.out.println(taskManager.subtasks);





    }
}