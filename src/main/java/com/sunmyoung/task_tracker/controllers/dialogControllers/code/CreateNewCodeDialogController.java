package com.sunmyoung.task_tracker.controllers.dialogControllers.code;

import com.sunmyoung.task_tracker.pojos.Code;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;

public class CreateNewCodeDialogController {

    @FXML
    @Getter
    private TextField clientTF;

    @FXML
    @Getter
    private TextField codeTF;

    @FXML
    @Getter
    private CheckBox combiCB;

    @FXML
    @Getter
    private TextField frameSizeTF;

    @FXML
    @Getter
    private TextField meshSizeTF;

    @FXML
    @Getter
    private TextField meshTF;

    @FXML
    @Getter
    private TextField biasTF;

    @FXML
    @Getter
    private TextField tensionTF;

    @FXML
    void enableMeshSizeTF(ActionEvent event) {
        meshSizeTF.setDisable(!combiCB.isSelected());
    }

    public void readFields(Code code) {
        code.setCode(codeTF.getText());
        code.setClient(clientTF.getText());
        code.setFrameSize(frameSizeTF.getText());
        code.setCombi(combiCB.isSelected() ? "COMBI" : "직견장");
        if (combiCB.isSelected()) {
            code.setMeshSize(meshSizeTF.getText());
        }
        code.setMesh(meshTF.getText());
        code.setTension(tensionTF.getText());
        code.setBias(biasTF.getText());
    }

    public void populateWindow(Code code) {
        codeTF.setText(code.getCode());
        clientTF.setText(code.getClient());
        frameSizeTF.setText(code.getFrameSize());
        if (code.getCombi().equals("COMBI")) {
            combiCB.setSelected(true);
            meshSizeTF.setDisable(false);
            meshSizeTF.setText(code.getMeshSize());
        }
        meshTF.setText(code.getMesh());
        tensionTF.setText(code.getTension());
        biasTF.setText(code.getBias());
    }
}
