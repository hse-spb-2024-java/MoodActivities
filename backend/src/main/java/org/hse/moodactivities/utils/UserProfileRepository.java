package org.hse.moodactivities.utils;


import org.hse.moodactivities.data.entities.postgres.AuthProvider;
import org.hse.moodactivities.data.entities.postgres.UserProfile;
import org.hse.moodactivities.data.utils.HibernateUtils;

import java.util.Optional;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class UserProfileRepository {
    public static Optional<UserProfile> findByLogin(AuthProvider provider,
                                                    String login) {
        try (var entityManager = HibernateUtils.getEntityManagerFactory().createEntityManager()) {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<UserProfile> criteriaQuery = criteriaBuilder.createQuery(UserProfile.class);
            Root<UserProfile> userProfile = criteriaQuery.from(UserProfile.class);

            criteriaQuery.select(userProfile).where(criteriaBuilder.and(
                    criteriaBuilder.equal(userProfile.get("login"), login),
                    criteriaBuilder.equal(userProfile.get("authProvider"), provider)
            ));

            UserProfile result = entityManager.createQuery(criteriaQuery).getSingleResult();
            return Optional.ofNullable(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<UserProfile> findByEmail(AuthProvider provider,
                                                    String email) {
        try (var entityManager = HibernateUtils.getEntityManagerFactory().createEntityManager()) {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<UserProfile> criteriaQuery = criteriaBuilder.createQuery(UserProfile.class);
            Root<UserProfile> userProfile = criteriaQuery.from(UserProfile.class);

            criteriaQuery.select(userProfile).where(criteriaBuilder.and(
                    criteriaBuilder.equal(userProfile.get("email"), email),
                    criteriaBuilder.equal(userProfile.get("authProvider"), provider)
            ));

            UserProfile result = entityManager.createQuery(criteriaQuery).getSingleResult();
            return Optional.ofNullable(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<UserProfile> findByOauthId(AuthProvider provider,
                                                      String oauthId) {
        try (var entityManager = HibernateUtils.getEntityManagerFactory().createEntityManager()) {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<UserProfile> criteriaQuery = criteriaBuilder.createQuery(UserProfile.class);
            Root<UserProfile> userProfile = criteriaQuery.from(UserProfile.class);

            criteriaQuery.select(userProfile).where(criteriaBuilder.and(
                    criteriaBuilder.equal(userProfile.get("oauthId"), oauthId),
                    criteriaBuilder.equal(userProfile.get("authProvider"), provider)
            ));

            UserProfile result = entityManager.createQuery(criteriaQuery).getSingleResult();
            return Optional.ofNullable(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<UserProfile> findById(String id) {
        try (var entityManager = HibernateUtils.getEntityManagerFactory().createEntityManager()) {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<UserProfile> criteriaQuery = criteriaBuilder.createQuery(UserProfile.class);
            Root<UserProfile> userProfile = criteriaQuery.from(UserProfile.class);

            criteriaQuery.select(userProfile).where(criteriaBuilder.and(
                    criteriaBuilder.equal(userProfile.get("id"), Long.valueOf(id))
            ));

            UserProfile result = entityManager.createQuery(criteriaQuery).getSingleResult();
            return Optional.ofNullable(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static boolean saveEntity(UserProfile userProfile) {
        try (var entityManager = HibernateUtils.getEntityManagerFactory().createEntityManager()) {
            entityManager.persist(userProfile);
            return true;
        } catch (Exception e) {
            return true;
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
