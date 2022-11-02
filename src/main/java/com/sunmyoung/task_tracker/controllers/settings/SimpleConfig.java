package com.sunmyoung.task_tracker.controllers.settings;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class SimpleConfig {
    private static final Path path = Paths.get("src/main/resources/cfg");
    private static List<String> lines;

    static {
        try {
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        try (FileWriter writer = new FileWriter(path.toFile())) {
            writer.write(url);
            writer.write("\n");
            writer.write(username);
            writer.write("\n");
            writer.write(password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
