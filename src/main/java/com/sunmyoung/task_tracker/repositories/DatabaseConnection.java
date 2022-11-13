package com.sunmyoung.task_tracker.repositories;

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
//    @Getter
//    private static SessionFactory sessionFactory;

    @Getter
    private static EntityManagerFactory entityManagerFactory;

//    public static Boolean connect(String url, String username, String password) {
//        try {
//            StandardServiceRegistry registry = configureBuilder(url, username, password).build();
//            Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
//            sessionFactory = metadata.getSessionFactoryBuilder().build();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }

//    private static StandardServiceRegistryBuilder configureBuilder(String url, String username, String password) {
//        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
//        builder.configure("hibernate.cfg.xml");
//        builder.applySetting(AvailableSettings.URL, url);
//        builder.applySetting(AvailableSettings.USER, username);
//        builder.applySetting(AvailableSettings.PASS, password);
//        return builder;
//    }

    public static Boolean connect(String url, String username, String password) {
        try {
            Properties properties = new Properties();
            properties.put("jakarta.persistence.jdbc.url", url); //"jdbc:mysql://localhost:3306/sunmyoung?useSSL=false"
            properties.put("jakarta.persistence.jdbc.user", username);
            properties.put("jakarta.persistence.jdbc.password", password);
            entityManagerFactory = Persistence.createEntityManagerFactory("com.sunmyoung.task_tracker", properties);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
