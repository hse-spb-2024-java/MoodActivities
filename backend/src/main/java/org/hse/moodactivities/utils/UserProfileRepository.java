package org.hse.moodactivities.utils;


import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hse.moodactivities.data.entities.postgres.UserProfile;
import org.hse.moodactivities.data.utils.HibernateUtils;

import java.util.Optional;

public class UserProfileRepository {
    public static Optional<UserProfile> findByLogin(String login) {
        try (var entityManager = HibernateUtils.getEntityManagerFactory().createEntityManager()) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<UserProfile> cq = cb.createQuery(UserProfile.class);
            Root<UserProfile> userProfile = cq.from(UserProfile.class);

            cq.select(userProfile).where(cb.equal(userProfile.get("login"), login));

            UserProfile result = entityManager.createQuery(cq).getSingleResult();
            return Optional.ofNullable(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<UserProfile> findByEmail(String email) {
        try (var entityManager = HibernateUtils.getEntityManagerFactory().createEntityManager()) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<UserProfile> cq = cb.createQuery(UserProfile.class);
            Root<UserProfile> userProfile = cq.from(UserProfile.class);

            cq.select(userProfile).where(cb.equal(userProfile.get("email"), email));

            UserProfile result = entityManager.createQuery(cq).getSingleResult();
            return Optional.ofNullable(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static UserProfile createUserProfile(String login, String unhashedPassword) {
        // Выглядит как говно если честно
        // Это руинит абстракции когда внезапно надо начинать думать об исключениях
        // Что с этим делать мне пока неясно
        // Возможно нужно рулить через Either
        UserProfile newProfile = new UserProfile(login, unhashedPassword);
        HibernateUtils.inTransaction(
                em -> {
                    em.persist(newProfile);
                }
        );
        return newProfile;
    }
}
