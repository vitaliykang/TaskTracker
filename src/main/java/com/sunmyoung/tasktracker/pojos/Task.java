package com.sunmyoung.tasktracker.pojos;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToMany(mappedBy = "task", fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @Getter @Setter
    private List<Subtask> subtasks = new ArrayList<>();

    @Column(name = "deadline_date")
    private String deadlineDate;

    @Column(name = "deadline_note", length = 45)
    private String deadlineNote;

//    @Setter
//    private String deadline;

    @Column(name = "order_date")
    private Instant orderDate;

    @Column(name = "order_date_str")
    private String orderDateStr;

    @Column(name = "client", length = 200)
    private String client;

    @Column(name = "count")
    private Integer count;

    @Column(name = "size", length = 45)
    private String size;

    @Column(name = "mesh", length = 45)
    private String mesh;

    @Column(name = "combi", length = 45)
    private String combi;

    @Column(name = "type", length = 45)
    private String type;

    @Column(name = "tensioning", length = 45)
    private String tensioning;

    @Column(name = "coating", length = 45)
    private String coating;

    @Column(name = "washing", length = 45)
    private String washing;

    @Column(name = "exposure", length = 45)
    private String exposure;

    @Column(name = "is_complete")
    @Getter @Setter
    private Boolean isComplete;

    @Column(name = "bias")
    @Getter @Setter
    private Double bias;

    @Column(name = "is_aluminum")
    @Getter @Setter
    private Boolean isAluminum;

    @Column(name = "emulsion")
    @Getter @Setter
    private String emulsion;

    @Column(name = "note", length = 250)
    @Getter @Setter
    private String note;

    public Task() {
        orderDate = Instant.now();
        orderDateStr = dateToString(orderDate);
        isComplete = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(String deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public String getDeadlineNote() {
        return deadlineNote;
    }

    public void setDeadlineNote(String deadlineNote) {
        this.deadlineNote = deadlineNote;
    }

    public String getDeadline() {
        return String.format("%s - %s", deadlineDate, deadlineNote);
    }

    public Instant getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderDateStr() {
        return orderDateStr;
    }

    public void setOrderDateStr(String orderDateStr) {
        this.orderDateStr = orderDateStr;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMesh() {
        return mesh;
    }

    public void setMesh(String mesh) {
        this.mesh = mesh;
    }

    public String getCombi() {
        return combi;
    }

    public void setCombi(String combi) {
        this.combi = combi;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getWashing() {
        return washing;
    }

    public void setWashing(String washing) {
        this.washing = washing;
    }

    public String getExposure() {
        return exposure;
    }

    public void setExposure(String exposure) {
        this.exposure = exposure;
    }

    public String getAluminum() {
        return isAluminum ? "Aluminum" : "Regular";
    }

    @Override
    public String toString() {
        return String.format("%s - %s : %d %nSubtasks: %s", client, size, count, subtasks.toString());
    }

    private String dateToString(Instant time) {
        Date date = Date.from(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
}