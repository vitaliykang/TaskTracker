package com.sunmyoung.task_tracker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Utilities {
    /*
     * Path to the folder with the TaskTracker.jar
     */
    private static String pathStr = "";
    private static final byte[] SALT = {43, -92, -79, 81, 49, -26, 21, 53, -84, 109, 127, 120, -108, -20, 15, 120};

    static {
        String str = Objects.requireNonNull(Main.class.getResource("mainV2.fxml"), "mainV2.fxml not found").toString();
        try {
            pathStr = str.substring(str.indexOf('/') + 1, str.indexOf("TaskTracker.jar!"));
            pathStr = pathStr.replaceAll("%20", " ");
        } catch (Exception e) {
            Utilities.printStatus("Could not generate a path to the root folder.", Utilities.class);
        }
    }

    /**
     * Prints the status with a timestamp in the console.
     * @param status - message to print in the console.
     */
    public static void printStatus(String status, Class className) {
        LocalDateTime time = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        System.out.printf("%s: %s (%s)%n", time, status, className.getSimpleName());
    }

    /**
     * Reads the content of the file and returns it as a List.
     * @param fileName - path to the file from the root folder.
     * @return - List<String> that contains all the lines from the file.
     */
    public static List<String> readFromFile(String fileName) {
        List<String> content = new ArrayList<>();
        Path path = Paths.get(pathStr.concat(fileName));
        try {
            content = Files.readAllLines(path);
        } catch (IOException e) {
            System.out.printf("File \"%s\" not found. %n", path.toString());
            System.out.println("Creating a new file");
            createFile(path);
        }

        return content;
    }

    private static void createFile(Path path) {
        File file = new File(path.toString());
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("File successfully created.");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to create a new file.");
                ErrorMessage.show(e);
            }
        }
    }

    /**
     * Writes provided list of string to the file.
     * @param fileName - path to the file from the root folder.
     * @param content - List<String> that needs to be written to the file.
     */
    public static void writeToFile(String fileName, List<String> content) {
        Path path = Paths.get(pathStr.concat(fileName));
        if (!path.toFile().exists()) {
            createFile(path);
        }

        try (FileWriter fileWriter = new FileWriter(path.toFile())){
            StringBuilder stringBuilder = new StringBuilder();
            for (String line : content) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            fileWriter.write(stringBuilder.toString());
        } catch (IOException e) {
            System.out.printf("Failed to write to the file \"%s\". %n", path);
            ErrorMessage.show(e);
        }
    }

    /**
     * Appends a single String to the given file.
     * @param fileName - path to the file from the root folder.
     * @param newLine - single string that needs to be written to the file.
     */
    public static void appendToFile(String fileName, String newLine) {
        Path path = Paths.get(pathStr.concat(fileName));
        try (FileWriter fileWriter = new FileWriter(path.toFile(), true)) {
            fileWriter.write(newLine);
            fileWriter.write("\n");
        } catch (IOException e) {
            System.out.printf("Failed to write to the file \"%s\". %n", path);
            ErrorMessage.show(e);
        }
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(SALT);
            byte[] hash = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                sb.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorMessage.show(e);
        }
        return null;
    }

    private static byte[] generateSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return salt;
    }
}
