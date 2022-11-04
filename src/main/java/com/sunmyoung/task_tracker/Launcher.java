package com.sunmyoung.task_tracker;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

public class Launcher {
    public static void main(String[] args) throws Exception{
        try {
            Main.main(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
