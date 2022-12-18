package com.sunmyoung.task_tracker.repositories;

import com.sunmyoung.task_tracker.ErrorMessage;
import com.sunmyoung.task_tracker.Utilities;
import com.sunmyoung.task_tracker.pojos.Password;
import jakarta.persistence.EntityManager;

public class PasswordRepository {

    public static String getPassword() {
        String password = "";
        EntityManager entityManager = DatabaseConnection.getEntityManagerFactory().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Password pass = entityManager.createQuery("from Password p where p.id = 1", Password.class).getSingleResult();
            password = pass.getPassword();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorMessage.show(e);
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }
        return password;
    }

    public static void updatePassword(String newPassword) {
        EntityManager entityManager = DatabaseConnection.getEntityManagerFactory().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Password password = entityManager.createQuery("from Password p where p.id = 1", Password.class).getSingleResult();
            password.setPassword(Utilities.encodePassword(newPassword));
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            ErrorMessage.show(e);
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }
    }
}
