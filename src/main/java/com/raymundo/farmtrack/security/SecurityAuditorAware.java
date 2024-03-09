package com.raymundo.farmtrack.security;

import com.raymundo.farmtrack.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityAuditorAware implements AuditorAware<String> {

    private final SecurityContextHolderStrategy holderStrategy;

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(holderStrategy.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(UserEntity.class::cast)
                .map(UserEntity::getEmail);
    }
}
