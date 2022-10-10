package com.sunmyoung.tasktracker.repositories;

import com.sunmyoung.tasktracker.pojos.Task;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.PersistenceException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TaskRepository {
    public static void save(Task task) {
        Session session = Database.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(task);
            transaction.commit();
        } catch (PersistenceException persistenceException) {
            transaction = session.beginTransaction();
            Task repoTask = get(task.getId());
            repoTask.setSubtasks(task.getSubtasks());
            repoTask.setBias(task.getBias());
            repoTask.setClient(task.getClient());
            repoTask.setCoating(task.getCoating());
            repoTask.setCombi(task.getCombi());
            repoTask.setCount(task.getCount());
            repoTask.setDeadlineDate(task.getDeadlineDate());
            repoTask.setDeadlineNote(task.getDeadlineNote());
            repoTask.setEmulsion(task.getEmulsion());
            repoTask.setExposure(task.getExposure());
            repoTask.setIsAluminum(task.getIsAluminum());
            repoTask.setIsCombi(task.getIsCombi());
            repoTask.setIsComplete(task.getIsComplete());
            repoTask.setMesh(task.getMesh());
            repoTask.setNote(task.getNote());
            repoTask.setOrderDate(task.getOrderDate());
            repoTask.setOrderDateStr(task.getOrderDateStr());
            repoTask.setSize(task.getSize());
            repoTask.setTensioning(task.getTensioning());
            repoTask.setType(task.getType());
            repoTask.setWashing(task.getWashing());
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
    }

    public static Task get(long taskId) {
        Session session = Database.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Task result = session.createQuery("from Task t where t.id = :id", Task.class).setParameter("id", taskId).uniqueResult();
            transaction.commit();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }

        return null;
    }

    public static List<Task> getUnfinishedTasks() {
        Session session = Database.getSessionFactory().openSession();;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            List<Task> result = session.createQuery("From Task t where t.isComplete = false", Task.class).getResultList();
            transaction.commit();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null)
                transaction.rollback();
        } finally {
            session.close();
        }
        return null;
    }
}
