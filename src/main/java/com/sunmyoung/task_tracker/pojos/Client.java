package com.sunmyoung.task_tracker.pojos;

import jakarta.persistence.*;

@Entity
@Table(name = "clients")
public class Client implements Comparable<Client>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(length = 45)
    private String code;

    @Column(length = 100)
    private String client;

    @Column(length = 45)
    private String type;

    @Column(length = 45)
    private String manager;

    public Client() {
    }

    public Client(String[] clientArr) {
        id = Long.parseLong(clientArr[0]);
        code = clientArr[1];
        client = clientArr[2];
        type = clientArr[3];
        manager = clientArr[4];
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    @Override
    public int compareTo(Client o) {
        return this.code.compareTo(o.code);
    }

    public String convertToString() {
        return String.format("%d :: %s :: %s :: %s :: %s", id, code, client, type, manager);
    }

    @Override
    public String toString() {
        return String.format("%s: %s (%s) %s", code, client, type, manager);
    }
}
