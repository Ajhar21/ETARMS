package com.ztrios.etarms.identity.service;

import com.ztrios.etarms.audit.model.AuditAction;
import com.ztrios.etarms.audit.service.AuditService;
import com.ztrios.etarms.common.exception.ResourceNotFoundException;
import com.ztrios.etarms.identity.entity.User;
import com.ztrios.etarms.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditService auditService;

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        auditService.log(
                AuditAction.LOGIN,
                "User",
                user.getId().toString(),
                "User logged in"
        );

        return userRepository.save(user);
    }
}
