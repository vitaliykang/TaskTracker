package com.sunmyoung.task_tracker.pojos;

import com.sunmyoung.task_tracker.controllers.dialogControllers.CreateOrderDialogControllerV2;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "tasks")
public class ActiveTask implements TaskInterface, Comparable<ActiveTask>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Model> subtasks = new HashSet<>();

    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<InspectionReport> inspectionReports = new HashSet<>();

    @Column(length = 40)
    private String code;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "shipment_from")
    private String shipmentFrom;

    @Column(name = "client_company", length = 200)
    private String client;

    @Column(name = "type", length = 45)
    private String type;

    @Column(name = "person_in_charge", length = 45)
    private String personInCharge;

    @Column(length = 45)
    private String film;

    @Column(name = "frame_size", length = 45)
    private String frameSize;

    @Column(length = 45)
    private String mesh;

    @Column(length = 45)
    private String combi;

    @Column(name = "new_frame")
    private Boolean isNewFrame;

    @Column(name = "frame_condition", length = 60)
    private String frameCondition;

    @Column(name = "bias")
    private String bias;

    @Column(length = 40)
    private String tension;

    @Column(length = 40)
    private String emulsion;

    @Column(name = "date_in")
    private LocalDate dateIn;

    @Column(name = "shipping_method", length = 40)
    private String shippingMethod;

    @Column(name = "date_out", length = 40)
    private LocalDate dateOut;

    @Column(name = "deadline_note", length = 40)
    private String deadlineNote;

    @Column(name = "print_position", length = 40)
    private String printPosition;

    @Column(name = "order_note", length = 250)
    private String orderNote;

    @Column(name = "count")
    private Integer count;

    @Column(name = "is_aluminum")
    private Boolean isAluminum;

    @Column(name = "tensioning", length = 45)
    private String tensioning;

    @Column(name = "coating", length = 45)
    private String coating;

    @Column(name = "exposure", length = 45)
    private String exposure;

    @Column(name = "packaging", length = 45)
    private String packaging;

    public ActiveTask() {
    }

    public String getDeadline() {
        try {
            return String.format("%s - %s", dateOut.format(CreateOrderDialogControllerV2.getKoreanFormat()), deadlineNote);
        } catch (NullPointerException e) {
            return deadlineNote;
        }
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

    @Override
    public int compareTo(ActiveTask otherTask) {
        int compareByDateOut = dateOut.compareTo(otherTask.dateOut);
        return compareByDateOut != 0 ? compareByDateOut :
                client.compareTo(otherTask.getClient());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Set<Model> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(Set<Model> subtasks) {
        this.subtasks = subtasks;
    }

    public Set<InspectionReport> getInspectionReports() {
        return inspectionReports;
    }

    public void setInspectionReports(Set<InspectionReport> inspectionReports) {
        this.inspectionReports = inspectionReports;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public String getShipmentFrom() {
        return shipmentFrom;
    }

    public void setShipmentFrom(String shipmentFrom) {
        this.shipmentFrom = shipmentFrom;
    }

    @Override
    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getPersonInCharge() {
        return personInCharge;
    }

    public void setPersonInCharge(String personInCharge) {
        this.personInCharge = personInCharge;
    }

    @Override
    public String getFilm() {
        return film;
    }

    public void setFilm(String film) {
        this.film = film;
    }

    @Override
    public String getFrameSize() {
        return frameSize;
    }

    public void setFrameSize(String frameSize) {
        this.frameSize = frameSize;
    }

    @Override
    public String getMesh() {
        return mesh;
    }

    public void setMesh(String mesh) {
        this.mesh = mesh;
    }

    @Override
    public String getCombi() {
        return combi;
    }

    public void setCombi(String combi) {
        this.combi = combi;
    }

    public Boolean getIsNewFrame() {
        return isNewFrame;
    }

    public void setIsNewFrame(Boolean newFrame) {
        isNewFrame = newFrame;
    }

    public String getFrameCondition() {
        return frameCondition;
    }

    public void setFrameCondition(String frameCondition) {
        this.frameCondition = frameCondition;
    }

    @Override
    public String getBias() {
        return bias;
    }

    public void setBias(String bias) {
        this.bias = bias;
    }

    @Override
    public String getTension() {
        return tension;
    }

    public void setTension(String tension) {
        this.tension = tension;
    }

    @Override
    public String getEmulsion() {
        return emulsion;
    }

    public void setEmulsion(String emulsion) {
        this.emulsion = emulsion;
    }

    @Override
    public LocalDate getDateIn() {
        return dateIn;
    }

    public void setDateIn(LocalDate dateIn) {
        this.dateIn = dateIn;
    }

    @Override
    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    @Override
    public LocalDate getDateOut() {
        return dateOut;
    }

    public void setDateOut(LocalDate dateOut) {
        this.dateOut = dateOut;
    }

    @Override
    public String getDeadlineNote() {
        return deadlineNote;
    }

    public void setDeadlineNote(String deadlineNote) {
        this.deadlineNote = deadlineNote;
    }

    @Override
    public String getPrintPosition() {
        return printPosition;
    }

    public void setPrintPosition(String printPosition) {
        this.printPosition = printPosition;
    }

    @Override
    public String getOrderNote() {
        return orderNote;
    }

    public void setOrderNote(String orderNote) {
        this.orderNote = orderNote;
    }

    @Override
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Boolean getIsAluminum() {
        return isAluminum;
    }

    public void setIsAluminum(Boolean aluminum) {
        isAluminum = aluminum;
    }

    public String getTensioning() {
        return tensioning;
    }

    public void setTensioning(String tensioning) {
        this.tensioning = tensioning;
    }

    public String getCoating() {
        return coating;
    }

    public void setCoating(String coating) {
        this.coating = coating;
    }

    public String getExposure() {
        return exposure;
    }

    public void setExposure(String exposure) {
        this.exposure = exposure;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }
}