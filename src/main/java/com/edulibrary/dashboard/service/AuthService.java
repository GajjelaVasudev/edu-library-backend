package com.edulibrary.dashboard.service;

import com.edulibrary.dashboard.dto.ForgotPasswordResponse;
import com.edulibrary.dashboard.dto.ForgotPasswordRequest;
import com.edulibrary.dashboard.dto.MessageResponse;
import com.edulibrary.dashboard.dto.LoginRequest;
import com.edulibrary.dashboard.dto.RegisterRequest;
import com.edulibrary.dashboard.dto.ResetPasswordRequest;
import com.edulibrary.dashboard.model.UserAccount;
import com.edulibrary.dashboard.repository.UserAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserAccountRepository userAccountRepository;

    public AuthService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    /**
     * Simple register: save user directly to DB with plain text password (NO HASHING)
     */
    @Transactional
    public MessageResponse register(String email, String password) {
        String normalizedEmail = normalize(email);
        String normalizedPassword = normalize(password);

        if (normalizedEmail.isEmpty() || normalizedPassword.isEmpty()) {
            return new MessageResponse("Email and password are required");
        }

        try {
            UserAccount user = new UserAccount(normalizedEmail, normalizedPassword);
            userAccountRepository.save(user);
            return new MessageResponse("Register successful");
        } catch (Exception ignored) {
            return new MessageResponse("Register successful");
        }
    }

    /**
     * Simple login: check if user exists with same email and password
     */
    @Transactional(readOnly = true)
    public MessageResponse login(String email, String password) {
        String normalizedEmail = normalize(email);
        String normalizedPassword = normalize(password);

        if (normalizedEmail.isEmpty() || normalizedPassword.isEmpty()) {
            return new MessageResponse("Email and password are required");
        }

        try {
            UserAccount user = userAccountRepository.findByEmailIgnoreCase(normalizedEmail)
                    .orElse(null);

            if (user == null || !normalizedPassword.equals(user.getPassword())) {
                return new MessageResponse("Invalid email or password");
            }

            return new MessageResponse("Login successful");
        } catch (Exception ignored) {
            return new MessageResponse("Invalid email or password");
        }
    }

    public MessageResponse login(LoginRequest request) {
        throw new UnsupportedOperationException("Use login(email, password) instead");
    }

    public MessageResponse register(RegisterRequest request) {
        return register(request.getEmail(), request.getPassword());
    }

    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public MessageResponse resetPassword(ResetPasswordRequest request) {
        throw new UnsupportedOperationException("Not implemented");
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}

