package com.sunmyoung.task_tracker.controllers.settings;

import com.sunmyoung.task_tracker.Main;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SimpleConfig {
    private static Path path;
    private static List<String> lines = new ArrayList<>();

    static {
        try {
            String str = Main.class.getResource("main.fxml").toString();
            //path to the folder where the jar file is located
            String pathStr = str.substring(str.indexOf('/') + 1, str.indexOf("TaskTracker.jar!")).concat("cfg");
            path = Paths.get(pathStr);
            lines = Files.readAllLines(path);
            System.out.println(lines);
        } catch (Exception e) {
            e.printStackTrace();
            lines.add("url");
            lines.add("username");
            lines.add("password");
        }
    }

    public static void main(String[] args) {

    }

    public static String getURL() {
        return lines.get(0);
    }

    public static String getUsername() {
        return lines.get(1);
    }

    public static String getPassword() {
        return lines.get(2);
    }

    public static void saveInfo(String url, String username, String password) {
        if (path != null) {
            try (FileWriter fileWriter = new FileWriter(path.toFile())) {
                fileWriter.write(url);
                fileWriter.write("\n");
                fileWriter.write(username);
                fileWriter.write("\n");
                fileWriter.write(password);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
