package com.sunmyoung.task_tracker.pojos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "inspection_reports")
public class InspectionReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "coatings")
    private String coatings;

    @Column(name = "date")
    private String date;

    @Column(name = "exposure_inspection")
    private String exposureInspection;

    @Column(name = "final_inspection")
    private String finalInspection;

    @Column(name = "lot")
    private String lot;

    @Column(name = "mesh_thickness")
    private String meshThickness;

    @Column(name = "one_day_aging")
    private String oneDayAging;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "total_thickness")
    private String totalThickness;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    @Getter @Setter
    private ActiveTask task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "completed_task_id")
    @Getter @Setter
    private CompletedTask completedTask;

    public InspectionReport() {
        lot = "Lot";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoatings() {
        return coatings;
    }

    public void setCoatings(String coatings) {
        this.coatings = coatings;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExposureInspection() {
        return exposureInspection;
    }

    public void setExposureInspection(String exposureInspection) {
        this.exposureInspection = exposureInspection;
    }

    public String getFinalInspection() {
        return finalInspection;
    }

    public void setFinalInspection(String finalInspection) {
        this.finalInspection = finalInspection;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public String getMeshThickness() {
        return meshThickness;
    }

    public void setMeshThickness(String meshThickness) {
        this.meshThickness = meshThickness;
    }

    public String getOneDayAging() {
        return oneDayAging;
    }

    public void setOneDayAging(String oneDayAging) {
        this.oneDayAging = oneDayAging;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getTotalThickness() {
        return totalThickness;
    }

    public void setTotalThickness(String totalThickness) {
        this.totalThickness = totalThickness;
    }

}