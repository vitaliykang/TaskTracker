package com.sunmyoung.task_tracker.pojos;

import com.sunmyoung.task_tracker.controllers.dialogControllers.CreateOrderDialogControllerV2;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "completed_tasks")
public class CompletedTask implements TaskInterface{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToMany(mappedBy = "completedTask", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @Getter @Setter
    private Set<Model> subtasks = new HashSet<>();

    @OneToMany(mappedBy = "completedTask", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @Getter @Setter
    private Set<InspectionReport> inspectionReports = new HashSet<>();

    @Column(length = 40)
    private String code;

    @Column(name = "bias")
    private String bias;

    @Column(name = "client_company", length = 200)
    private String client;

    @Column(name = "coating", length = 45)
    private String coating;

    @Column(name = "combi", length = 45)
    private String combi;

    @Column(name = "count")
    private Integer count;

    @Column(name = "date_in")
    private LocalDate dateIn;

    @Column(name = "date_out")
    private LocalDate dateOut;

    @Column(name = "deadline_note", length = 40)
    private String deadlineNote;

    @Column(name = "emulsion", length = 40)
    private String emulsion;

    @Column(name = "exposure", length = 45)
    private String exposure;

    @Column(name = "film", length = 45)
    private String film;

    @Column(name = "frame_size", length = 45)
    private String frameSize;

    @Column(name = "is_aluminum")
    private Boolean isAluminum;

    @Column(name = "new_frame")
    private Boolean isNewFrame;

    @Column(name = "frame_condition", length = 60)
    private String frameCondition;

    @Column(name = "mesh", length = 45)
    private String mesh;

    @Column(name = "order_note", length = 250)
    private String orderNote;

    @Column(name = "packaging", length = 45)
    private String packaging;

    @Column(name = "person_in_charge", length = 45)
    private String personInCharge;

    @Column(name = "print_position", length = 40)
    private String printPosition;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "shipment_from")
    private String shipmentFrom;

    @Column(name = "shipping_method", length = 40)
    private String shippingMethod;

    @Column(name = "status")
    private String status;

    @Column(name = "tension", length = 40)
    private String tension;

    @Column(name = "tensioning", length = 45)
    private String tensioning;

    @Column(name = "type", length = 45)
    private String type;

    public CompletedTask() {}

    public CompletedTask(ActiveTask task) {
        code = task.getCode();
        serialNumber = task.getSerialNumber();
        shipmentFrom = task.getShipmentFrom();
        client = task.getClient();
        personInCharge = task.getPersonInCharge();
        film = task.getFilm();
        frameSize = task.getFrameSize();
        mesh = task.getMesh();
        combi = task.getCombi();
        isNewFrame = task.getIsNewFrame();
        frameCondition = task.getFrameCondition();
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

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBias() {
        return bias;
    }

    public void setBias(String bias) {
        this.bias = bias;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String clientCompany) {
        this.client = clientCompany;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCoating() {
        return coating;
    }

    public void setCoating(String coating) {
        this.coating = coating;
    }

    public String getCombi() {
        return combi;
    }

    public void setCombi(String combi) {
        this.combi = combi;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public LocalDate getDateIn() {
        return dateIn;
    }

    public void setDateIn(LocalDate dateIn) {
        this.dateIn = dateIn;
    }

    public LocalDate getDateOut() {
        return dateOut;
    }

    public void setDateOut(LocalDate dateOut) {
        this.dateOut = dateOut;
    }

    public String getDeadlineNote() {
        return deadlineNote;
    }

    public void setDeadlineNote(String deadlineNote) {
        this.deadlineNote = deadlineNote;
    }

    public String getEmulsion() {
        return emulsion;
    }

    public void setEmulsion(String emulsion) {
        this.emulsion = emulsion;
    }

    public String getExposure() {
        return exposure;
    }

    public void setExposure(String exposure) {
        this.exposure = exposure;
    }

    public String getFilm() {
        return film;
    }

    public void setFilm(String film) {
        this.film = film;
    }

    public String getFrameSize() {
        return frameSize;
    }

    public void setFrameSize(String frameSize) {
        this.frameSize = frameSize;
    }

    public Boolean getIsAluminum() {
        return isAluminum;
    }

    public void setIsAluminum(Boolean isAluminum) {
        this.isAluminum = isAluminum;
    }

    public Boolean getIsNewFrame() {
        return isNewFrame;
    }

    public void setIsNewFrame(Boolean newFrame) {
        this.isNewFrame = newFrame;
    }

    public String getFrameCondition() {
        return frameCondition;
    }

    public void setFrameCondition(String frameCondition) {
        this.frameCondition = frameCondition;
    }

    public String getMesh() {
        return mesh;
    }

    public void setMesh(String mesh) {
        this.mesh = mesh;
    }

    public String getOrderNote() {
        return orderNote;
    }

    public void setOrderNote(String orderNote) {
        this.orderNote = orderNote;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public String getPersonInCharge() {
        return personInCharge;
    }

    public void setPersonInCharge(String personInCharge) {
        this.personInCharge = personInCharge;
    }

    public String getPrintPosition() {
        return printPosition;
    }

    public void setPrintPosition(String printPosition) {
        this.printPosition = printPosition;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getShipmentFrom() {
        return shipmentFrom;
    }

    public void setShipmentFrom(String shipmentFrom) {
        this.shipmentFrom = shipmentFrom;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTension() {
        return tension;
    }

    public void setTension(String tension) {
        this.tension = tension;
    }

    public String getTensioning() {
        return tensioning;
    }

    public void setTensioning(String tensioning) {
        this.tensioning = tensioning;
    }

}