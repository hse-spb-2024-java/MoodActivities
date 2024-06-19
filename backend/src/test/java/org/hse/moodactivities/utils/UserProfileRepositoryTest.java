package org.hse.moodactivities.utils;

import org.hse.moodactivities.data.entities.postgres.AuthProvider;
import org.hse.moodactivities.data.entities.postgres.UserProfile;
import org.hse.moodactivities.data.utils.HibernateUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserProfileRepositoryTest {

    private EntityManagerFactory emf;

    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("org.hse.moodactivities.data");
        HibernateUtils.setEntityManagerFactory(emf);
    }

    @AfterEach
    void tearDown() {
        emf.close();
    }

    @Test
    void testFindByLogin() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        UserProfile user = new UserProfile(AuthProvider.PLAIN, "testLogin", "test@example.com", "password", null);
        em.persist(user);
        em.getTransaction().commit();
        em.close();

        Optional<UserProfile> found = UserProfileRepository.findByLogin(AuthProvider.PLAIN, "testLogin");
        assertTrue(found.isPresent());
        assertEquals("test@example.com", found.get().getEmail());
    }

    @Test
    void testFindByEmail() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        UserProfile user = new UserProfile(AuthProvider.PLAIN, "testLogin", "test@example.com", "password", null);
        em.persist(user);
        em.getTransaction().commit();
        em.close();

        Optional<UserProfile> found = UserProfileRepository.findByEmail(AuthProvider.PLAIN, "test@example.com");
        assertTrue(found.isPresent());
        assertEquals("testLogin", found.get().getLogin());
    }

    @Test
    void testFindByOauthId() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        UserProfile user = new UserProfile(AuthProvider.GOOGLE, "testLogin", "test@example.com", "password", "oauthId123");
        em.persist(user);
        em.getTransaction().commit();
        em.close();

        Optional<UserProfile> found = UserProfileRepository.findByOauthId(AuthProvider.GOOGLE, "oauthId123");
        assertTrue(found.isPresent());
        assertEquals("test@example.com", found.get().getEmail());
    }

    @Test
    void testFindById() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        UserProfile user = new UserProfile(AuthProvider.PLAIN, "testLogin", "test@example.com", "password", null);
        em.persist(user);
        em.getTransaction().commit();
        em.close();

        Optional<UserProfile> found = UserProfileRepository.findById(String.valueOf(user.getId()));
        assertTrue(found.isPresent());
        assertEquals("testLogin", found.get().getLogin());
    }

    @Test
    void testSaveEntity() {
        UserProfile user = new UserProfile(AuthProvider.PLAIN, "testLogin", "test@example.com", "password", null);
        boolean result = UserProfileRepository.saveEntity(user);
        assertTrue(result);

        Optional<UserProfile> found = UserProfileRepository.findById(String.valueOf(user.getId()));
        assertTrue(found.isPresent());
        assertEquals("testLogin", found.get().getLogin());
    }

    @Test
    void testCreatePlainUserProfile() {
        UserProfile user = UserProfileRepository.createPlainUserProfile("plainLogin", "plain@example.com", "password");
        assertNotNull(user);
        assertEquals("plain@example.com", user.getEmail());
    }

    @Test
    void testCreateGoogleUserProfile() {
        UserProfile user = UserProfileRepository.createGoogleUserProfile("google@example.com", "oauthId123");
        assertNotNull(user);
        assertEquals("google@example.com", user.getEmail());
    }
}
