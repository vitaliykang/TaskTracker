package com.sunmyoung.task_tracker.repositories;

import com.sunmyoung.task_tracker.Utilities;
import com.sunmyoung.task_tracker.pojos.ActiveTask;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.PersistenceException;
import java.util.*;

public class TaskRepository {
    public static void save(ActiveTask task) {
        Session session = DatabaseConnection.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(task);
            transaction.commit();
        } catch (PersistenceException persistenceException) {
            transaction = session.beginTransaction();
            ActiveTask repoTask = get(task.getId());
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

    public static void delete(long taskId) {
        Session session = DatabaseConnection.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            session.createQuery("delete ActiveTask t where t.id = :taskId").setParameter("taskId", taskId)
                    .executeUpdate();

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

    public static ActiveTask get(long taskId) {
        Session session = DatabaseConnection.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            ActiveTask result = session.createQuery("from ActiveTask t join fetch t.subtasks where t.id = :id", ActiveTask.class).setParameter("id", taskId).uniqueResult();
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

    public static List<ActiveTask> getActiveTasks() {
        Session session = DatabaseConnection.getSessionFactory().openSession();;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            List<ActiveTask> resultList = session.createQuery("from ActiveTask", ActiveTask.class).getResultList();
            transaction.commit();
            Set<ActiveTask> taskSet = new HashSet<>(resultList);
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
