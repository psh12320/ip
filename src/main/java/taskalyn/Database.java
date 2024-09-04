package taskalyn;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;

public class Database {
    private static final String DIRECTORY = "./data";
    private static final String FILE_NAME = "taskalyn.txt";
    private static final Path FILE_PATH = Paths.get(DIRECTORY, FILE_NAME);

    public Database() {
        try {
            List<String> txtLines = Files.readAllLines(FILE_PATH);
            if (Files.notExists(FILE_PATH)) {
                Files.createDirectory(Paths.get(DIRECTORY));
                Files.createFile(FILE_PATH);
            }
        } catch (NoSuchFileException e) {
            this.createDatabase();
        } catch (IOException e) {
            System.out.println("Error getting database: " + e.getMessage());
        }
    }

    public void createDatabase() {
        try {
            Files.createDirectories(FILE_PATH.getParent());
            Files.createFile(FILE_PATH);
        } catch (IOException e) {
            System.out.println("Error creating database: " + e.getMessage());
        }
    }

    public void writeToDatabase(List<String> lines) {
        try {
            Files.write(FILE_PATH, lines);
        } catch (IOException e) {
            System.out.println("Error writing database: " + e.getMessage());
        }
    }

    public List<String> readFromDatabase() throws IOException {
        return Files.readAllLines(FILE_PATH);
    }

    public int getDatabaseSize() {
        try {
            List<String> txtLines = readFromDatabase();
            return txtLines.size();
        } catch (IOException e) {
            System.out.println("Error checking Database Size: " + e.getMessage());
        }
    }
}
