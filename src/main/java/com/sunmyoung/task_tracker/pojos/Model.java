package com.sunmyoung.task_tracker.pojos;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "subtasks")
public class Model implements Comparable<Model> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "completed_task_id")
    @Getter @Setter
    private CompletedTask completedTask;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    @Getter @Setter
    private ActiveTask task;

    @Column(name = "count")
    private String count = "";

    @Column(name = "print", length = 254)
    private String print = "";

    @Column(name = "thickness")
    private String thickness = "";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPrint() {
        return print;
    }

    public void setPrint(String print) {
        this.print = print;
    }

    public String getThickness() {
        return thickness;
    }

    public void setThickness(String thickness) {
        this.thickness = thickness;
    }

    @Override
    public int compareTo(Model o) {
        return this.print.compareTo(o.print);
    }
}