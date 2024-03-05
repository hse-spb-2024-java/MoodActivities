package org.hse.moodactivities.data.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.function.Consumer;

public class HibernateUtils {
    private static final EntityManagerFactory emFactory;

    static {
        try {
            emFactory = Persistence.createEntityManagerFactory("org.hse.moodactivities.backend");
        } catch (Throwable ex) {
            System.err.print("Failed to initialize emF." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return emFactory;
    }

    public static void inTransaction(Consumer<EntityManager> work) {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = getEntityManagerFactory().createEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();
            work.accept(entityManager);
            transaction.commit();
        } catch (Throwable ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw ex;
        }
    }
}
