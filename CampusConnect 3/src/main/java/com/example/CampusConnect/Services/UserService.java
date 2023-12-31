package com.example.CampusConnect.Services;

import com.example.CampusConnect.Entities.CCuser;
import com.example.CampusConnect.Entities.Item;
import com.example.CampusConnect.Repositories.CCuserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    private final CCuserRepository userRepository;
    private final EmailService emailService;

    @Autowired
    public UserService(CCuserRepository userRepository/*, PasswordEncoder passwordEncoder*/, EmailService emailService) {
        this.userRepository = userRepository;
        //this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }


    public boolean verifyEmailForPasswordReset(String token) {
        // Find user by password reset token
        Optional<CCuser> userOpt = userRepository.findByPasswordResetToken(token);

        if (userOpt.isPresent()) {
            CCuser user = userOpt.get();

            // Check if the email is already verified for password reset
            if (!user.getIsEmailVerifiedForPasswordChange()) {
                user.setIsEmailVerifiedForPasswordChange(true);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    public boolean checkEmailVerificationStatus(String email) {
        Optional<CCuser> user = userRepository.findByEmail(email);
        CCuser cCuser;
        if(user.isPresent()){
            cCuser = user.get();
            return cCuser.getIsEmailVerifiedForPasswordChange();
        }
        // Handle the case where the user is not found or another error occurs
        throw new RuntimeException("User not found");
    }

    public boolean checkCredentials(String email, String password) {
        Optional<CCuser> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return false;
        }
        CCuser user = userOpt.get();
        return user.getPassword().equals(password); // Direct string comparison (unsafe!)
    }

    // Register a new user
    public void registerUser(CCuser user) {
        // Encrypt the password

        // Set the verification token
        user.generateNewVerificationToken();

        // Save the user to the database
        userRepository.save(user);

        // Send verification email
        String verificationUrl = "http://localhost:5173/verify-email?token=" + user.getVerificationToken();
        emailService.sendVerificationEmail(user.getEmail(), verificationUrl);

    }
    
    public boolean sendPasswordResetEmail(String email) {
        Optional<CCuser> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            CCuser user = userOptional.get();
            user.generateNewPasswordResetToken();
            userRepository.save(user);

            // Update the resetUrl to point to the new verification page
            String resetUrl = "http://localhost:5173/password-reset-verification?token=" + user.getPasswordResetToken();
            emailService.sendPasswordResetEmail(email, resetUrl);
            return true;
        }

        return false;
    }

    // Verify the user
    public boolean verifyUser(String token) {
        CCuser user = userRepository.findByVerificationToken(token);

        if (user != null && !user.isEmailVerified()) {
            user.verifyEmail();
            userRepository.save(user);
            return true;
        }

        return false;
    }

    public boolean verifyPassword(String email, String password) {
        Optional<CCuser> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return false; // Email not found
        }
        CCuser user = userOpt.get();
        return user.getPassword().equals(password); // Direct string comparison (unsafe!)
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean verifyIsEmailVerified(String email) {
        Optional<CCuser> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            CCuser user = userOpt.get();
            return user.isEmailVerified();
        }
        return false; // Email not found, consider how you want to handle this case
    }

    public Long getUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(CCuser::getUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public boolean changePassword(String email, String newPassword) {
        Optional<CCuser> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            CCuser user = userOpt.get();
            user.setPassword(newPassword); // Directly set the new password
            userRepository.save(user); // Save the user with the new password
            return true;
        }
        return false;
    }
    public String getEmailByUserId(Long userId) {
        return userRepository.findById(userId)
                .map(CCuser::getEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public CCuser findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
    }

    @Transactional
    public void addItemToUser(Item item, Long userId) {
        CCuser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.addItem(item);
        item.setUser(user);
    }
}
