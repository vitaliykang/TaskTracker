package com.sunmyoung.task_tracker.pojos;

import com.sunmyoung.task_tracker.controllers.dialogControllers.CreateOrderDialogControllerV2;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "completed_tasks")
public class CompletedTask implements TaskInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    @Getter @Setter
    private Long id;

    @OneToMany(mappedBy = "completedTask", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @Getter @Setter
    private Set<Model> subtasks = new HashSet<>();

    @OneToMany(mappedBy = "completedTask", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @Getter @Setter
    private Set<InspectionReport> inspectionReports = new HashSet<>();

    @Column(name = "serial_number")
    @Getter @Setter
    private String serialNumber;

    @Column(name = "shipment_from")
    @Getter @Setter
    private String shipmentFrom;

    @Column(name = "client_company", length = 200)
    @Getter @Setter
    private String client;

    @Column(name = "person_in_charge", length = 45)
    @Getter @Setter
    private String personInCharge;

    @Column(length = 45)
    @Getter @Setter
    private String film;

    @Column(name = "frame_size", length = 45)
    @Getter @Setter
    private String frameSize;

    @Column(length = 45)
    @Getter @Setter
    private String mesh;

    @Column(length = 45)
    @Getter @Setter
    private String combi;

    @Column(name = "new_frame")
    @Getter @Setter
    private Boolean isNewFrame;

    @Column(name = "bias")
    @Getter @Setter
    private String bias;

    @Column(length = 40)
    @Getter @Setter
    private String tension;

    @Column(length = 40)
    @Getter @Setter
    private String emulsion;

    @Column(name = "date_in")
    @Getter @Setter
    private LocalDate dateIn;

    @Column(name = "shipping_method", length = 40)
    @Getter @Setter
    private String shippingMethod;

    @Column(name = "date_out", length = 40)
    @Getter @Setter
    private LocalDate dateOut;

    @Column(name = "deadline_note", length = 40)
    @Getter @Setter
    private String deadlineNote;

    @Column(name = "print_position", length = 40)
    @Getter @Setter
    private String printPosition;

    @Column(name = "order_note", length = 250)
    @Getter @Setter
    private String orderNote;

    @Column(name = "count")
    @Getter @Setter
    private Integer count;

    @Column(name = "type", length = 45)
    @Getter @Setter
    private String type;

    @Column(name = "is_aluminum")
    @Getter @Setter
    private Boolean isAluminum;

    @Column(name = "tensioning", length = 45)
    @Getter @Setter
    private String tensioning;

    @Column(name = "coating", length = 45)
    @Getter @Setter
    private String coating;

    @Column(name = "exposure", length = 45)
    @Getter @Setter
    private String exposure;

    @Column(name = "packaging", length = 45)
    @Getter @Setter
    private String packaging;

    @Column
    @Getter @Setter
    private String status;

    public CompletedTask() {

    }

    public CompletedTask(ActiveTask task) {
        serialNumber = task.getSerialNumber();
        shipmentFrom = task.getShipmentFrom();
        client = task.getClient();
        personInCharge = task.getPersonInCharge();
        film = task.getFilm();
        frameSize = task.getFrameSize();
        mesh = task.getMesh();
        combi = task.getCombi();
        isNewFrame = task.getIsNewFrame();
        bias = task.getBias();
        tension = task.getTension();
        emulsion = task.getEmulsion();
        dateIn = task.getDateIn();
        shippingMethod = task.getShippingMethod();
        dateOut = task.getDateOut();
        deadlineNote = task.getDeadlineNote();
        printPosition = task.getPrintPosition();
        orderNote = task.getOrderNote();
        count = task.getCount();
        type = task.getType();
        isAluminum = task.getIsAluminum();
        tensioning = task.getTensioning();
        coating = task.getCoating();
        exposure = task.getExposure();
        packaging = task.getPackaging();
    }

    public String getDeadline() {
        return String.format("%s - %s", dateOut.format(CreateOrderDialogControllerV2.getKoreanFormat()), deadlineNote);
    }

    public String getFrameNewOld() {
        return isNewFrame ? "신규프레임" : "지급프레임";
    }

    @Override
    public String toString() {
        return String.format("Client - %s, " +
                "Size - [%s], " +
                "Count - %d %n", client, frameSize, count);
    }
}
