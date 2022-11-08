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
    private static String pathStr = "";

    static {
        String str = Main.class.getResource("mainV2.fxml").toString();
        try {
            pathStr = str.substring(str.indexOf('/') + 1, str.indexOf("TaskTracker.jar!"));
        } catch (Exception e) {
            System.out.println("No cfg file detected. Loading default parameters.");
        }
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
            System.out.printf("File \"%s\" not found. %n", path.toString());
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
            System.out.printf("File \"%s\" not found. %n", path);
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

    public static void main(String[] args) {
        List<String> list = List.of("인피세라::(양산) 이출일 부장\n" +
                "삼화콘덴서공업 (MLC)::이진수 대리\n" +
                "삼화콘덴서공업 (CI)::이진수 대리\n" +
                "엠펙스 메디칼::(개발) 이진희 님");
        writeToFile("C:\\Users\\admin\\IdeaProjects\\TaskTracker\\out\\artifacts\\TaskTracker_jar\\data\\clients", list);
    }
}
