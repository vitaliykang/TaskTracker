package com.sunmyoung.tasktracker.repositories;

import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;

public class DatabaseConnection {
    @Getter
    private static SessionFactory sessionFactory;

    public static Boolean connect(String url, String username, String password) {
        try {
            StandardServiceRegistry registry = configureBuilder(url, username, password).build();
            Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
            sessionFactory = metadata.getSessionFactoryBuilder().build();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static StandardServiceRegistryBuilder configureBuilder(String url, String username, String password) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.configure("hibernate.cfg.xml");
        builder.applySetting(AvailableSettings.URL, url);
        builder.applySetting(AvailableSettings.USER, username);
        builder.applySetting(AvailableSettings.PASS, password);
        return builder;
    }
}
