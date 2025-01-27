package controllers;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerExceptionTest {

    @Test
    void shouldThrowExceptionForInvalidFilePath() {
        File invalidFile = new File("/invalid_path/test_tasks.csv");
        assertThrows(RuntimeException.class, () -> {
            FileBackedTaskManager.loadFromFile(invalidFile);
        }, "Должно выбрасываться исключение при неверном пути к файлу");
    }

    @Test
    void shouldThrowExceptionForCorruptedFile() {
        File corruptedFile = new File("corrupted_test.csv");
        try {
            corruptedFile.createNewFile();
        } catch (IOException e) {
            fail("Не удалось создать поврежденный тестовый файл.");
        }

        assertThrows(ManagerException.class, () -> {
            FileBackedTaskManager.loadFromFile(corruptedFile);
        }, "Ожидается ошибка при загрузке поврежденного файла");

        corruptedFile.delete();
    }

    @Test
    void shouldNotThrowExceptionForValidFile() {
        File validFile = new File("valid_test.csv");
        assertDoesNotThrow(() -> {
            FileBackedTaskManager manager = new FileBackedTaskManager(validFile);
            manager.save();
        }, "Не должно выбрасываться исключение при корректной работе с файлом");

        validFile.delete();
    }
}
