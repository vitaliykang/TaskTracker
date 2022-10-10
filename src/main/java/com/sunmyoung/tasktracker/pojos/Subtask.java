package com.sunmyoung.tasktracker.pojos;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "subtasks")
public class Subtask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Getter
    @Setter
    private Integer id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    @Getter @Setter
    private Task task;

    @Column(name = "thickness")
    @Getter @Setter
    private String thickness;

    @Column(name = "print", length = 254)
    @Getter @Setter
    private String print;

    @Column
    @Getter @Setter
    private String count;

    public String toString()
    {
        return String.format("%s - %s - %s", print, thickness, count);
    }
}
