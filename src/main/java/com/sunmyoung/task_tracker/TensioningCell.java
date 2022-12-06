package com.sunmyoung.task_tracker;

import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DefaultStringConverter;

public class TensioningCell<ActiveTask> extends TextFieldTableCell<ActiveTask, String> {

    public TensioningCell() {
        setConverter(new DefaultStringConverter());
    }

    @Override
    public void updateItem(String item, boolean empty) {
        if(item == null || empty) {
            this.setStyle("");
            return;
        }

        item = item.toUpperCase();
        super.updateItem(item, false);

        if (item.equals("OK")) {
            this.setStyle("-fx-background-color: #59ff27;");
        } else {
            this.setStyle("-fx-background-color: red;");
        }
        this.setText(item);
    }
}
