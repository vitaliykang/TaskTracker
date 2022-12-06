package com.sunmyoung.task_tracker;

import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DefaultStringConverter;

public class ConditionalFormattingCell<ActiveTask> extends TextFieldTableCell<ActiveTask, String> {
    private final String positiveInput;

    public ConditionalFormattingCell(String positiveInput) {
        this.positiveInput = positiveInput.toUpperCase();
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

        if (item.equals(positiveInput)) {
            this.setStyle("-fx-background-color: #59ff27;");
        } else {
            this.setStyle("-fx-background-color: red;");
        }
        this.setText(item);
    }
}
