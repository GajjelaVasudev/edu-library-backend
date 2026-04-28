package com.edulibrary.dashboard.service;

import com.edulibrary.dashboard.dto.AuthResponse;
import com.edulibrary.dashboard.dto.AuthUserResponse;
import com.edulibrary.dashboard.dto.ForgotPasswordResponse;
import com.edulibrary.dashboard.dto.ForgotPasswordRequest;
import com.edulibrary.dashboard.dto.LoginRequest;
import com.edulibrary.dashboard.dto.MessageResponse;
import com.edulibrary.dashboard.dto.RegisterRequest;
import com.edulibrary.dashboard.dto.ResetPasswordRequest;
import com.edulibrary.dashboard.exception.UnauthorizedException;
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
    public AuthUserResponse register(String email, String password) {
        UserAccount user = new UserAccount(email, password);
        UserAccount saved = userAccountRepository.save(user);
        return new AuthUserResponse(saved.getId(), email, email, "USER", null);
    }

    /**
     * Simple login: check if user exists with same email and password
     */
    @Transactional(readOnly = true)
    public MessageResponse login(String email, String password) {
        UserAccount user = userAccountRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        // Plain text password comparison (NO HASHING)
        if (!user.getPassword().equals(password)) {
            throw new UnauthorizedException("Invalid email or password");
        }

        return new MessageResponse("Login successful");
    }

    // Keeping other method signatures for backward compatibility but not implementing them
    public AuthResponse login(LoginRequest request) {
        throw new UnsupportedOperationException("Use login(email, password) instead");
    }

    public AuthUserResponse register(RegisterRequest request) {
        throw new UnsupportedOperationException("Use register(email, password) instead");
    }

    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public MessageResponse resetPassword(ResetPasswordRequest request) {
        throw new UnsupportedOperationException("Not implemented");
    }
}

