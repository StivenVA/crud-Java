import org.junit.jupiter.api.*;
import org.project.util.ManagerStorage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class ManagerStorageTest {

    private static final String TEST_DIRECTORY = "test_directory";

    @BeforeAll
    static void setup() {
        File testDir = new File(TEST_DIRECTORY);
        testDir.mkdir();
    }

    @AfterAll
    static void cleanup() {
        File testDir = new File(TEST_DIRECTORY);
        testDir.delete();
    }

    @Test
    void testSaveFile() throws IOException {

        File testFile = new File("test_file_to_save.txt");
        testFile.createNewFile();

        ManagerStorage managerStorage = new ManagerStorage(TEST_DIRECTORY);
        managerStorage.saveFile(testFile);

        Path destinationPath = Paths.get(TEST_DIRECTORY, testFile.getName());
        assertFalse(Files.exists(testFile.toPath()));
        assertTrue(Files.exists(destinationPath));
    }

    @Test
    void testSaveFileNonExistingDirectory() throws IOException {

        File directory = new File(TEST_DIRECTORY);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }

        Files.deleteIfExists(Paths.get(TEST_DIRECTORY));

        File testFile = new File("Archivo prueba.txt");
        testFile.createNewFile();

        ManagerStorage managerStorage = new ManagerStorage(TEST_DIRECTORY);
        managerStorage.saveFile(testFile);

        Path destinationPath = Paths.get(TEST_DIRECTORY, testFile.getName());
        assertTrue(Files.exists(destinationPath));
    }


    @Test
    void testSaveFileExistingDirectory() throws IOException {

        File testFile = new File(TEST_DIRECTORY + File.separator + "test_file_to_save.txt");
        testFile.createNewFile();

        Files.createDirectories(Paths.get(TEST_DIRECTORY));

        ManagerStorage managerStorage = new ManagerStorage(TEST_DIRECTORY);
        managerStorage.saveFile(testFile);

        Path destinationPath = Paths.get(TEST_DIRECTORY, testFile.getName());
        assertTrue(Files.exists(destinationPath)); // El archivo debe existir en su nueva ubicación
    }
}
