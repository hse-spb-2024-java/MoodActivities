package org.hse.moodactivities.data.entities.postgres;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "UserProfile")
@Table(name = "user_profile_data")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(unique = true)
    private String email;


    private String hashedPassword;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider authProvider = AuthProvider.PLAIN;

    private String oauthId;
    @Column(nullable = true)
    private String lastSurveyDate;

    @Column(nullable = false)
    private Integer timeDiff;

    @Column(nullable = true)
    private String notificationsToken;

    public UserProfile() {
    }

    private void initPlain(String login,
                           String email,
                           String unhashedPassword) {
        this.login = login;
        this.email = email;
        this.hashedPassword = BCrypt.withDefaults().hashToString(12, unhashedPassword.toCharArray());
        this.timeDiff = 0;
    }

    private void initGoogle(String email,
                            String id) {
        this.login = email;
        this.email = email;
        this.oauthId = id;
    }

    public UserProfile(AuthProvider provider,
                       String login,
                       String email,
                       String unhashedPassword,
                       String id) {
        switch (provider) {
            case PLAIN -> initPlain(login, email, unhashedPassword);
            case GOOGLE -> initGoogle(email, id);
        }
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getLastSurveyDate() {
        return lastSurveyDate;
    }

    public void setLastSurveyDate(String lastSurveyDate) {
        this.lastSurveyDate = lastSurveyDate;
    }

    public Integer getTimeDiff() {
        return timeDiff;
    }

    public void setTimeDiff(Integer timeDiff) {
        this.timeDiff = timeDiff;
    }

    public String getNotificationsToken() {
        return notificationsToken;
    }

    public void setNotificationsToken(String notificationsToken) {
        this.notificationsToken = notificationsToken;
    }

    public boolean validatePassword(String password) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword);
        return result.verified;
    }
}
