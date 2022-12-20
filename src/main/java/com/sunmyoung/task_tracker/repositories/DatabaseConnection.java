package com.sunmyoung.task_tracker.repositories;

import com.sunmyoung.task_tracker.Utilities;
import com.sunmyoung.task_tracker.pojos.ActiveTask;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.service.spi.ServiceException;

import java.net.ConnectException;
import java.util.Properties;

public class DatabaseConnection {
    @Getter
    private static EntityManagerFactory entityManagerFactory;

    public static Boolean connect(String url, String username, String password) {
        try {
            Properties properties = new Properties();
            properties.put("jakarta.persistence.jdbc.url", url); //"jdbc:mysql://localhost:3306/sunmyoung?useSSL=false"
            properties.put("jakarta.persistence.jdbc.user", username);
            properties.put("jakarta.persistence.jdbc.password", password);
            entityManagerFactory = Persistence.createEntityManagerFactory("com.sunmyoung.task_tracker", properties);
            Utilities.printStatus("Database Connection Established Successfully.", DatabaseConnection.class);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
