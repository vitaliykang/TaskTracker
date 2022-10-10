package com.sunmyoung.tasktracker;

import com.sunmyoung.tasktracker.controllers.TaskMonitorController;
import javafx.fxml.FXMLLoader;
import lombok.SneakyThrows;

import java.io.IOException;

public class Refresher implements Runnable{
    private final int time;
    private final TaskMonitorController controller;

    public Refresher(int time) {
        this.time = time * 1000;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TaskMonitor.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        controller = loader.getController();
    }

    @Override
    @SneakyThrows
    public void run() {
        while (true) {
            Thread.sleep(time);

            System.out.println("table refreshed");
        }
    }
}
