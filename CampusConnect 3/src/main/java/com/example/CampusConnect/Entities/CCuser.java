package com.example.CampusConnect.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "CCusers")
public class CCuser {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items = new ArrayList<>();

    // New field to store verification token
    private String verificationToken;
    @Getter
    private String passwordResetToken;


    public boolean getIsEmailVerifiedForPasswordChange() {
        return isEmailVerifiedForPasswordChange;
    }

    public void setIsEmailVerifiedForPasswordChange(boolean emailVerifiedForPasswordChange) {
        isEmailVerifiedForPasswordChange = emailVerifiedForPasswordChange;
    }

    private boolean isEmailVerifiedForPasswordChange;

    // New field to store email verification status
    private boolean isEmailVerified;

    public CCuser(String email, String password) {
        this.email = email;
        this.password = password;
        this.isEmailVerified = false; // Default to false until email is verified
        this.verificationToken = UUID.randomUUID().toString(); // Generate unique token
    }

    public CCuser() {
        // No-args constructor
    }
    public void addItem(Item item) {
        items.add(item);
        item.setUser(this);
    }

    public void changePassword(String password){
        setPassword(password);
    }

    // Method to generate a new verification token
    public void generateNewVerificationToken() {
        this.verificationToken = UUID.randomUUID().toString();
    }

    // Method to verify the user's email
    public void verifyEmail() {
        this.isEmailVerified = true;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void generateNewPasswordResetToken() {
        // Generate token logic here
        this.passwordResetToken = UUID.randomUUID().toString();
        this.isEmailVerifiedForPasswordChange = false;
    }
}
