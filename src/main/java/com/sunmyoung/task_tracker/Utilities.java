package com.sunmyoung.task_tracker;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Utilities {
    /**
     * Path to the folder with the TaskTracker.jar
     */
    private static String pathStr;

    static {
        String str = Main.class.getResource("main.fxml").toString();
        pathStr = str.substring(str.indexOf('/') + 1, str.indexOf("TaskTracker.jar!"));
    }

    /**
     * Prints the status with a timestamp in the console.
     * @param status - message to print in the console.
     */
    public static void printStatus(String status) {
        LocalDateTime time = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        System.out.printf("%s: %s %n", time, status);
    }

    /**
     * Reads the content of the file and returns it as a List.
     * @param fileName - name of the file with extension.
     * @return - List<String> that contains all the lines from the file.
     */
    public static List<String> readFromFile(String fileName) {
        List<String> content = new ArrayList<>();
        Path path = Paths.get(pathStr.concat(fileName));
        try {
            content = Files.readAllLines(path);
        } catch (IOException e) {
            System.out.printf("File \"%s\" not found. %n", fileName);
        }

        return content;
    }

    /**
     * Writes provided list of string to the file.
     * @param fileName - file in the same folder as TaskTracker.jar.
     * @param content - List<String> that needs to be written to the file.
     */
    public static void writeToFile(String fileName, List<String> content) {
        Path path = Paths.get(pathStr.concat(fileName));
        try (FileWriter fileWriter = new FileWriter(path.toFile())){
            StringBuilder stringBuilder = new StringBuilder();
            for (String line : content) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            fileWriter.write(stringBuilder.toString());
        } catch (IOException e) {
            System.out.printf("File \"%s\" not found. %n", fileName);
        }
    }

    /**
     * Appends a single String to the given file.
     * @param fileName - file in the same folder as TaskTracker.jar.
     * @param newLine - single string that needs to be written to the file.
     */
    public static void appendToFile(String fileName, String newLine) {
        Path path = Paths.get(pathStr.concat(fileName));
        try (FileWriter fileWriter = new FileWriter(path.toFile(), true)) {
            fileWriter.write(newLine);
            fileWriter.write("\n");
        } catch (IOException e) {
            System.out.printf("File \"%s\" not found. %n", fileName);
        }
    }
}
