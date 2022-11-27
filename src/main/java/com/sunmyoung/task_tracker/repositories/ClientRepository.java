package com.sunmyoung.task_tracker.repositories;

import com.sunmyoung.task_tracker.pojos.Client;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ClientRepository {

    public static List<Client> findAll() {
        List<Client> result = null;
        EntityManager entityManager = DatabaseConnection.getEntityManagerFactory().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            result = entityManager.createQuery("from Client", Client.class).getResultList();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }
        return result;
    }

    public static Client save(Client client) {
        EntityManager entityManager = DatabaseConnection.getEntityManagerFactory().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(client);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }
        return client;
    }

    public static void update(Client client) {
        EntityManager entityManager = DatabaseConnection.getEntityManagerFactory().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            Client repoClient = entityManager.createQuery("from Client c where c.id = :id", Client.class).setParameter("id", client.getId()).getSingleResult();
            repoClient.setClient(client.getClient());
            repoClient.setCode(client.getCode());
            repoClient.setType(client.getType());
            repoClient.setManager(client.getManager());
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }
    }

    public static void delete(long id) {
        EntityManager entityManager = DatabaseConnection.getEntityManagerFactory().createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.createQuery("delete Client c where c.id = :id").setParameter("id", id).executeUpdate();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }
    }
}
