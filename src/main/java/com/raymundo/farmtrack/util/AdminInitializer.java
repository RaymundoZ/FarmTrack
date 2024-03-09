package com.raymundo.farmtrack.util;

import com.raymundo.farmtrack.entity.UserEntity;
import com.raymundo.farmtrack.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initAdmin() {
        Optional<UserEntity> optional = userRepository.findByEmail("admin@mail.ru");
        if (optional.isEmpty()) {
            UserEntity user = new UserEntity();
            user.setName("admin");
            user.setSurname("admin");
            user.setPatronymic("admin");
            user.setEmail("admin@mail.ru");
            user.setPassword(passwordEncoder.encode("admin"));
            user.setRole(Role.ADMIN);
            user.setIsEnabled(true);
            userRepository.save(user);
        }
    }
}
