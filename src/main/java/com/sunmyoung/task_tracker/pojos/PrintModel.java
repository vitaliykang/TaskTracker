package com.sunmyoung.task_tracker.pojos;

import lombok.Getter;

public class PrintModel {
    @Getter
    private String print;
    @Getter
    private String thickness;

    public PrintModel(Model model) {
        print = model.getPrint();
        thickness = model.getThickness();
    }
}
