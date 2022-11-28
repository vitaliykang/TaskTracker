package com.sunmyoung.task_tracker.controllers.dialogControllers;

import com.sunmyoung.task_tracker.pojos.ActiveTask;
import com.sunmyoung.task_tracker.pojos.Model;
import com.sunmyoung.task_tracker.repositories.TaskRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.*;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Scale;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PrintOrderDialog {

    @FXML
    private Label
            serialNumberLabel,
            customerLabel,
            samegiLabel,
            filmLabel,
            frameConditionLabel,

            dateInLabel,
            dateOutLabel,
            deliveryTimeLabel,
            pesongLabel,
            noteLabel,

            frameSizeLabel,
            meshLabel,
            combiLabel,
            countLabel,
            biasLabel,
            tensionLabel,

            printPositionLabel,
            emulsionLabel;

    @FXML
    private TableColumn<Model, String>
            modelCol,
            thicknessCol,
            countCol;

    @FXML
    private TableView<Model> printingTableView;

    @FXML
    private AnchorPane pane;

    @Getter @Setter
    private ActiveTask task;

    private final ObservableList<Model> content = FXCollections.observableArrayList();

    @FXML
    public void print(ActionEvent event) {
        print();
    }

    public void print() {
        WritableImage screenshot = pane.snapshot(null, null);

        PrinterJob printerJob = PrinterJob.createPrinterJob();
        Paper paper = printerJob.getJobSettings().getPageLayout().getPaper();
        PageLayout pageLayout = printerJob.getPrinter().createPageLayout(paper, PageOrientation.LANDSCAPE, Printer.MarginType.HARDWARE_MINIMUM);
        double scaleX = pageLayout.getPrintableWidth() / screenshot.getWidth();
        double scaleY = pageLayout.getPrintableHeight() / screenshot.getHeight();
        double scale = Math.min(scaleX, scaleY);
        ImageView printNode = new ImageView(screenshot);
        Scene scene = pane.getScene();
        printNode.getTransforms().add(new Scale(scale, scale));
        printerJob.getJobSettings().setPageLayout(pageLayout);

        printerJob.showPrintDialog(scene.getWindow());

        if (printerJob != null) {
            if (printerJob.printPage(pane)) {
                printerJob.endJob();
            }
        }
    }
    
    public void initialize() {
        initTableView();
    }

    public void populateTableView() {
        task = TaskRepository.get(task.getId());
        List<Model> modelList = new ArrayList<>(Objects.requireNonNull(task, "Failed to fetch subtasks").getSubtasks());
        content.addAll(modelList);
        printingTableView.setItems(content);
    }

    private void initTableView() {
        modelCol.setCellValueFactory(new PropertyValueFactory<>("print"));
        thicknessCol.setCellValueFactory(new PropertyValueFactory<>("thickness"));
        countCol.setCellValueFactory(new PropertyValueFactory<>("count"));
    }

    public void populateWindow() {
        serialNumberLabel.setText(task.getSerialNumber());
        if (task.getType().equals("") || task.getType() == null) {
            customerLabel.setText(String.format("%s - %s", task.getClient(), task.getPersonInCharge()));
        } else {
            customerLabel.setText(String.format("%s (%s) - %s", task.getClient(), task.getType(), task.getPersonInCharge()));
        }
        samegiLabel.setText(task.getShipmentFrom().equals("null") ? "" : task.getShipmentFrom());
        filmLabel.setText(task.getFilm().equals("null") ? "" : task.getFilm());
        frameConditionLabel.setText(task.getFrameCondition().equals("null") ? "" : task.getFrameCondition());

        dateInLabel.setText(String.format("%d년 %d월 %d일", task.getDateIn().getYear(), task.getDateIn().getMonthValue(), task.getDateIn().getDayOfMonth()));
        dateOutLabel.setText(String.format("%d년 %d월 %d일", task.getDateOut().getYear(), task.getDateOut().getMonthValue(), task.getDateOut().getDayOfMonth()));
        deliveryTimeLabel.setText(task.getDeadlineNote().equals("null") ? "" : task.getDeadlineNote());
        pesongLabel.setText(task.getShippingMethod().equals("null") ? "" : task.getShippingMethod());
        noteLabel.setText(task.getOrderNote());

        frameSizeLabel.setText(task.getFrameSize());
        meshLabel.setText(task.getMesh());
        combiLabel.setText(task.getCombi());
        countLabel.setText(task.getCount().toString());
        biasLabel.setText(task.getBias());
        tensionLabel.setText(task.getTension());

        printPositionLabel.setText(task.getPrintPosition());
        emulsionLabel.setText(task.getEmulsion());
    }

}
