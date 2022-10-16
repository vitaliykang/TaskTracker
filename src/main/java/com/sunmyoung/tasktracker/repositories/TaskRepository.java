package com.sunmyoung.tasktracker.repositories;

import com.sunmyoung.tasktracker.pojos.Task;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.PersistenceException;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
            repoTask.setDateOut(task.getDateOut());
            repoTask.setDeadlineNote(task.getDeadlineNote());
            repoTask.setEmulsion(task.getEmulsion());
            repoTask.setExposure(task.getExposure());
            repoTask.setIsAluminum(task.getIsAluminum());
            repoTask.setCombi(task.getCombi());
            repoTask.setIsComplete(task.getIsComplete());
            repoTask.setMesh(task.getMesh());
            repoTask.setOrderNote(task.getOrderNote());
            repoTask.setDateIn(task.getDateIn());
            repoTask.setFrameSize(task.getFrameSize());
            repoTask.setTensioning(task.getTensioning());
            repoTask.setType(task.getType());
            repoTask.setPackaging(task.getPackaging());
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
            Task result = session.createQuery("from Task t join fetch t.subtasks where t.id = :id", Task.class).setParameter("id", taskId).uniqueResult();
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
            List<Task> resultList = session.createQuery("From Task t join fetch t.subtasks where t.isComplete = false", Task.class).getResultList();
            transaction.commit();
            Set<Task> taskSet = new HashSet<>(resultList);
            resultList = new ArrayList<>(taskSet);
            Collections.sort(resultList);
            return resultList;
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
