package org.hse.moodactivities.data.entities.postgres;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.persistence.*;

@Entity(name="UserProfile")
@Table(name="user_profile_data")
public class UserProfile {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String hashedPassword;

    public UserProfile() {}

    public UserProfile(String login, String unhashedPassword) {
        this.login = login;
        this.hashedPassword = BCrypt.withDefaults().hashToString(12, unhashedPassword.toCharArray());
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
