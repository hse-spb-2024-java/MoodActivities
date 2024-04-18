package org.hse.moodactivities.data.entities.postgres;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.persistence.*;

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

    public UserProfile() {
    }

    private void initPlain(String login,
                           String email,
                           String unhashedPassword) {
        this.login = login;
        this.email = email;
        this.hashedPassword = BCrypt.withDefaults().hashToString(12, unhashedPassword.toCharArray());
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

    public boolean validatePassword(String password) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword);
        return result.verified;
    }
}
