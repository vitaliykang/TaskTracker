package com.sunmyoung.tasktracker.pojos;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface Task {
    String getSerialNumber();

    String getShipmentFrom();

    String getClient();

    String getPersonInCharge();

    String getFilm();

    String getFrameSize();

    String getMesh();

    String getCombi();

    Boolean getIsNewFrame();

    String getBias();

    String getTension();

    String getEmulsion();

    LocalDate getDateIn();

    String getShippingMethod();

    LocalDate getDateOut();

    String getDeadlineNote();

    String getPrintPosition();

    String getOrderNote();

    Set<Subtask> getSubtasks();

    Integer getCount();
}
