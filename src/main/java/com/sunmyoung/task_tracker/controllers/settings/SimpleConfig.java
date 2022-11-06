package com.sunmyoung.task_tracker.controllers.settings;

import com.sunmyoung.task_tracker.Main;
import com.sunmyoung.task_tracker.Utilities;

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
    private static final String CFG = "cfg";
    private static List<String> lines = new ArrayList<>();

    static {
        lines = Utilities.readFromFile(CFG);
        if (lines.size() == 0) {
            Utilities.printStatus("No cfg file found");
            lines.add("localhost:3306");
            lines.add("root");
            lines.add("wasd");
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
        List<String> info = List.of(url, username, password);
        Utilities.writeToFile(CFG, info);
    }
}
