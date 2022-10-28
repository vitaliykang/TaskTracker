package com.sunmyoung.tasktracker.pojos;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "inspection_reports")
public class InspectionReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    @Getter
    @Setter
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    @Getter @Setter
    private ActiveTask task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "completed_task_id")
    @Getter @Setter
    private CompletedTask completedTask;

    @Column
    @Getter @Setter
    private String lot;

    @Column(name = "serial_number")
    @Getter @Setter
    private String serialNumber;

    @Column(name = "one_day_aging")
    @Getter @Setter
    private String oneDayAging;

    @Column(name = "date")
    @Getter @Setter
    private String date;

    @Column(name = "mesh_thickness")
    @Getter @Setter
    private String meshThickness;

    @Column(name = "coatings")
    @Getter @Setter
    private String coatings;

    @Column(name = "total_thickness")
    @Getter @Setter
    private String totalThickness;

    @Column(name = "exposure_inspection")
    @Getter @Setter
    private String exposureInspection;

    @Column(name = "final_inspection")
    @Getter @Setter
    private String finalInspection;

    public InspectionReport() {
        lot = "Lot";
    }

    @Override
    public String toString() {
        return "InspectionReport{" +
                "lot='" + lot + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", oneDayAging='" + oneDayAging + '\'' +
                ", date='" + date + '\'' +
                ", meshThickness='" + meshThickness + '\'' +
                ", coatings='" + coatings + '\'' +
                ", totalThickness='" + totalThickness + '\'' +
                ", exposureInspection='" + exposureInspection + '\'' +
                ", finalInspection='" + finalInspection + '\'' +
                '}';
    }
}
