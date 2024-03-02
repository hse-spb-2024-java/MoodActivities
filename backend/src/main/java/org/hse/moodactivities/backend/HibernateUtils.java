package org.hse.moodactivities.backend;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.function.Consumer;

public class HibernateUtils {
    public EntityManagerFactory entityManagerFactory;

    protected void setUp() {
        entityManagerFactory = Persistence.createEntityManagerFactory("org.hse.moodactivities.backend");
    }

    void inTransaction(Consumer<EntityManager> work) {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();
            work.accept(entityManager);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
}
