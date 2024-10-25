package Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class File{
    protected static byte[] readFile(String path) {
        try {
            Path filePath = Paths.get(path);
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return null;
        }
    }

    protected static void printFileContent(byte[] data) {
        try {
            String content = new String(data);
            System.out.println("File Content:\n" + content);
        } catch (Exception e) {
            System.err.println("Error printing file content: " + e.getMessage());
        }
    }
}
