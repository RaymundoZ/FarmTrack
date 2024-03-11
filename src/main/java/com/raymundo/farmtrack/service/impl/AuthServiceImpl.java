package com.raymundo.farmtrack.service.impl;

import com.raymundo.farmtrack.dto.AuthDto;
import com.raymundo.farmtrack.dto.UserInfoDto;
import com.raymundo.farmtrack.entity.UserEntity;
import com.raymundo.farmtrack.exception.AuthException;
import com.raymundo.farmtrack.mapper.UserInfoMapper;
import com.raymundo.farmtrack.repository.UserRepository;
import com.raymundo.farmtrack.service.AuthService;
import com.raymundo.farmtrack.service.JwtService;
import com.raymundo.farmtrack.util.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authManager;
    private final SecurityContextHolderStrategy holderStrategy;
    private final UserInfoMapper userInfoMapper;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserInfoDto authenticate(AuthDto authDto) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                authDto.email(),
                authDto.password()
        );
        Authentication authentication;
        try {
            authentication = authManager.authenticate(token);
        } catch (AuthenticationException e) {
            throw AuthException.Code.BAD_CREDENTIALS.get();
        }

        SecurityContext securityContext = holderStrategy.createEmptyContext();
        securityContext.setAuthentication(authentication);
        holderStrategy.setContext(securityContext);

        UserEntity user = (UserEntity) authentication.getPrincipal();

        user.setAccessToken(jwtService.generateToken(user, TokenType.ACCESS));
        user.setRefreshToken(jwtService.generateToken(user, TokenType.REFRESH));

        return userInfoMapper.toDto(user);
    }

    @Override
    public UserInfoDto register(UserInfoDto userInfoDto) {
        UserEntity user = userInfoMapper.toEntity(userInfoDto);
        return userInfoMapper.toDto(userRepository.save(user));
    }
}