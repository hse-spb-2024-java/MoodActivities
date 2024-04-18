package org.hse.moodactivities.utils;


import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hse.moodactivities.data.entities.postgres.AuthProvider;
import org.hse.moodactivities.data.entities.postgres.UserProfile;
import org.hse.moodactivities.data.utils.HibernateUtils;

import java.util.Optional;

public class UserProfileRepository {
    public static Optional<UserProfile> findByLogin(AuthProvider provider,
                                                    String login) {
        try (var entityManager = HibernateUtils.getEntityManagerFactory().createEntityManager()) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<UserProfile> cq = cb.createQuery(UserProfile.class);
            Root<UserProfile> userProfile = cq.from(UserProfile.class);

            cq.select(userProfile).where(cb.and(
                    cb.equal(userProfile.get("login"), login),
                    cb.equal(userProfile.get("authProvider"), provider)
            ));

            UserProfile result = entityManager.createQuery(cq).getSingleResult();
            return Optional.ofNullable(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<UserProfile> findByEmail(AuthProvider provider,
                                                    String email) {
        try (var entityManager = HibernateUtils.getEntityManagerFactory().createEntityManager()) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<UserProfile> cq = cb.createQuery(UserProfile.class);
            Root<UserProfile> userProfile = cq.from(UserProfile.class);

            cq.select(userProfile).where(cb.and(
                    cb.equal(userProfile.get("email"), email),
                    cb.equal(userProfile.get("authProvider"), provider)
            ));

            UserProfile result = entityManager.createQuery(cq).getSingleResult();
            return Optional.ofNullable(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<UserProfile> findByOauthId(AuthProvider provider,
                                                      String oauthId) {
        try (var entityManager = HibernateUtils.getEntityManagerFactory().createEntityManager()) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<UserProfile> cq = cb.createQuery(UserProfile.class);
            Root<UserProfile> userProfile = cq.from(UserProfile.class);

            cq.select(userProfile).where(cb.and(
                    cb.equal(userProfile.get("oauthId"), oauthId),
                    cb.equal(userProfile.get("authProvider"), provider)
            ));

            UserProfile result = entityManager.createQuery(cq).getSingleResult();
            return Optional.ofNullable(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static UserProfile createPlainUserProfile(String login,
                                                     String email,
                                                     String unhashedPassword) {
        UserProfile newProfile = new UserProfile(
                AuthProvider.PLAIN,
                login,
                email,
                unhashedPassword,
                "");
        HibernateUtils.inTransaction(
                em -> {
                    em.persist(newProfile);
                }
        );
        return newProfile;
    }

    public static UserProfile createGoogleUserProfile(String email,
                                                      String oauthId) {
        UserProfile newProfile = new UserProfile(
                AuthProvider.GOOGLE,
                "",
                email,
                "",
                oauthId
        );
        HibernateUtils.inTransaction(
                em -> {
                    em.persist(newProfile);
                }
        );
        return newProfile;
    }
}
