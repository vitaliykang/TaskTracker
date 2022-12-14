package com.sunmyoung.task_tracker.pojos;

import jakarta.persistence.*;

@Entity
@Table(name = "codes")
public class Code implements Comparable<Code> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "code", length = 45)
    private String code = "";

    @Column(name = "client", length = 200)
    private String client = "";

    @Column(name = "frame_size", length = 45)
    private String frameSize = "";

    @Column(name = "mesh_size", length = 45)
    private String meshSize = "";

    @Column(name = "tension", length = 45)
    private String tension = "";

    @Column(name = "mesh", length = 100)
    private String mesh = "";

    @Column(length = 10)
    private String bias = "";

    @Column(name = "combi", length = 45)
    private String combi = "";

    @Column(name = "count")
    private String count = "0";

    @Override
    public String toString() {
        return "Code{" +
                "code='" + code + '\'' +
                ", client='" + client + '\'' +
                ", frameSize='" + frameSize + '\'' +
                ", tension='" + tension + '\'' +
                ", mesh='" + mesh + '\'' +
                ", bias='" + bias + '\'' +
                ", combi='" + combi + '\'' +
                '}';
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

    public String getFrameSize() {
        return frameSize;
    }

    public void setFrameSize(String frameSize) {
        this.frameSize = frameSize;
    }

    public String getMeshSize() {
        return meshSize;
    }

    public void setMeshSize(String meshSize) {
        this.meshSize = meshSize;
    }

    public String getTension() {
        return tension;
    }

    public void setTension(String tension) {
        this.tension = tension;
    }

    public String getMesh() {
        return mesh;
    }

    public void setMesh(String mesh) {
        this.mesh = mesh;
    }

    public String getBias() {
        return bias;
    }

    public void setBias(String bias) {
        this.bias = bias;
    }

    public String getCombi() {
        return combi;
    }

    public void setCombi(String combi) {
        this.combi = combi;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    @Override
    public int compareTo(Code o) {
        return this.code.equals("") ? this.client.compareTo(o.client) : this.code.compareTo(o.code);
    }
}