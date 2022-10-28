package com.sunmyoung.tasktracker.pojos;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "subtasks")
public class Subtask implements Comparable<Subtask>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Getter @Setter
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    @Getter @Setter
    private ActiveTask task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "completed_task_id")
    @Getter @Setter
    private CompletedTask completedTask;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(thickness, subtask.thickness) && Objects.equals(print, subtask.print) && Objects.equals(count, subtask.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(thickness, print, count);
    }

    @Override
    public int compareTo(Subtask o) {
        return Long.compare(id, o.id);
    }
}
