package fong.complete;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class FileIOTest {
    @Test
    public void test_read_file() {
        String path = "src/test/resources/fileio_test_data.txt";
        String fileData = FileIOTest.readFile(path);
        assertTrue(fileData.equals("This is a demo."));
    }

    @Test
    public void test_write_file() {
        String path = "src/test/resources/fileio_test_data.output.txt";
        String fileData = "This is a write demo.";
        FileIOTest.writeFile(path, fileData);

        String readData = FileIOTest.readFile(path);
        assertTrue(readData.equals(fileData));
    }

    @Test
    public void test_read_resource() {
        String resourceName = "fileio_test_data.txt";
        String fileData = FileIOTest.readSystemResource(resourceName);
        assertTrue(fileData.equals("This is a demo."));
    }


    public static String readSystemResource(String resourceName) {
        String content = null;
        try {
            String path = ClassLoader.getSystemResource(resourceName).getPath();
            content = Files.readString(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static String readFile(String filePath) {
        String content = null;
        try {
            content = Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static void writeFile(String filePath, String fileString) {
        try {
            Files.write(Paths.get(filePath), fileString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
