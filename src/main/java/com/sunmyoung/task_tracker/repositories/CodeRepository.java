package com.sunmyoung.task_tracker.repositories;

import com.sunmyoung.task_tracker.pojos.Code;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;

import java.util.List;

public class CodeRepository {
    /**
     * Saves the given code in the database. In case if a code with the given ID already exists, it will be overwritten.
     * @param code object that needs to be saved.
     */
    public static void save(Code code) {
        EntityManager entityManager = DatabaseConnection.getEntityManagerFactory().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(code);
            entityManager.getTransaction().commit();
        } catch (PersistenceException persistenceException) {
            entityManager.getTransaction().rollback();
            entityManager.getTransaction().begin();
            System.out.println("Specified code already exists in the database. Overwriting the existing code.");
            entityManager.merge(code);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            entityManager.getTransaction().rollback();
        }
        finally {
            entityManager.close();
        }
    }

    public static List<Code> findAll() {
        EntityManager entityManager = DatabaseConnection.getEntityManagerFactory().createEntityManager();
        List<Code> result = null;
        try {
            entityManager.getTransaction().begin();
            result = entityManager.createQuery("FROM Code", Code.class).getResultList();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }
        return result;
    }

    public static void delete(long id) {
        EntityManager entityManager = DatabaseConnection.getEntityManagerFactory().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.createQuery("DELETE Code c where c.id = :id").setParameter("id", id).executeUpdate();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            entityManager.getTransaction().rollback();
        }
    }
}
