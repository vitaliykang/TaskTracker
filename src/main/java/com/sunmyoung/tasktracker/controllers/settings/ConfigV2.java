package com.sunmyoung.tasktracker.controllers.settings;

import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.sunmyoung.tasktracker.controllers.settings.Config.setPassword;

public class ConfigV2 {
    private static final Path path = Paths.get("src/main/resources/hibernate.cfg.xml");

    @Getter
    private static String
            url,
            username,
            password;


    private static List<String> lines;

    static {
        try {
            lines = Files.readAllLines(path);
            lines.forEach(line -> {
                if (line.contains("url")) {
                    int start = line.indexOf("//") + 2;
                    int end = line.indexOf('/', start);
                    url = line.substring(start, end);
                } else if (line.contains("username")) {
                    int start = line.indexOf('>') + 1;
                    int end = line.indexOf('<', start);
                    username = line.substring(start, end);
                } else if (line.contains("password")) {
                    int start = line.indexOf('>') + 1;
                    int end = line.indexOf('<', start);
                    password = line.substring(start, end);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String changeValue(String newValue, String line) {
        int start = 0;
        int end = 0;
        if (line.contains("url")) {
            start = line.indexOf("//") + 2;
            end = line.indexOf('/', start);
        }
        else {
            start = line.indexOf(">") + 1;
            end = line.indexOf("<", start);
        }

        StringBuilder sb = new StringBuilder(line.substring(0, start));
        sb.append(newValue);
        sb.append(line.substring(end));
        return sb.toString();
    }

    @SneakyThrows
    public static void setCredentials(String url, String username, String password) {
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).contains("url")) {
                lines.set(i, changeValue(url, lines.get(i)));
            }
            else if (lines.get(i).contains("username")) {
                lines.set(i, changeValue(username, lines.get(i)));
            }
            else if (lines.get(i).contains("password")) {
                lines.set(i, changeValue(password, lines.get(i)));
            }
        }

        FileWriter writer = null;
        try {
            writer = new FileWriter(path.toFile());
            for (String line : lines) {
                writer.write(line);
                writer.write("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
