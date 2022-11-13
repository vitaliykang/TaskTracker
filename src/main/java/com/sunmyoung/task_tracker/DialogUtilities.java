package com.sunmyoung.task_tracker;

import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DialogUtilities {
    /**
     * Checks if TextFields specified in the highlightMap have data. Highlights TextFields that have no data.
     * @param highlightMap - map, containing TextFields and highlighting Rectangles.
     * @return - true, if all TextFields have data, otherwise - false.
     */
    public static boolean checkNonNullFields(Map<TextField, Rectangle> highlightMap) {
        List<TextField> list = new ArrayList<>(highlightMap.keySet());
        boolean flag = true;
        for (TextField field : list) {
            if (!hasData(field)) {
                highlightMap.get(field).setVisible(true);
                flag = false;
            }
        }
        return flag;
    }

    private static boolean hasData(TextField field) {
        return !(field.getText() == null || field.getText().equals(""));
    }
}
