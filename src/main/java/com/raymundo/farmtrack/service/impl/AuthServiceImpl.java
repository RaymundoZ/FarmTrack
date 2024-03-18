package com.raymundo.farmtrack.service.impl;

import com.raymundo.farmtrack.dto.AuthDto;
import com.raymundo.farmtrack.dto.UserInfoDto;
import com.raymundo.farmtrack.entity.UserEntity;
import com.raymundo.farmtrack.mapper.UserInfoMapper;
import com.raymundo.farmtrack.repository.UserRepository;
import com.raymundo.farmtrack.service.AuthService;
import com.raymundo.farmtrack.service.JwtService;
import com.raymundo.farmtrack.util.enumeration.TokenType;
import com.raymundo.farmtrack.util.exception.AuthException;
import com.raymundo.farmtrack.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link AuthService} interface for user authentication, registration,
 * and user management operations such as blocking and unblocking users.
 * <p>
 * It utilizes an {@link AuthenticationManager} for authenticating users,
 * a {@link SecurityContextHolderStrategy} for managing the security context,
 * a {@link JwtService} for generating JWT tokens, a {@link UserRepository} for accessing user data,
 * and a {@link UserInfoMapper} for mapping user entities to DTOs.
 *
 * @author RaymundoZ
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authManager;
    private final SecurityContextHolderStrategy holderStrategy;
    private final UserInfoMapper userInfoMapper;
    private final UserRepository userRepository;
    private final JwtService jwtService;


    /**
     * Authenticates a user using the provided authentication details.
     * <p>
     * This method takes an {@code AuthDto} object containing the user's authentication
     * credentials, including email and password. It attempts to authenticate the user
     * using these credentials by creating a {@code UsernamePasswordAuthenticationToken}
     * and passing it to the authentication manager ({@code authManager}). If authentication
     * fails due to bad credentials or a disabled account, appropriate exceptions are thrown.
     * <p>
     * Upon successful authentication, the user's authentication details are set in the security
     * context using the authentication holder strategy ({@code holderStrategy}). Access and refresh
     * tokens are generated for the authenticated user using the JWT service ({@code jwtService}),
     * and these tokens are set in the user entity.
     * <p>
     * Finally, the authenticated user entity is mapped to a {@code UserInfoDto} object using
     * {@code userInfoMapper} and returned.
     *
     * @param authDto An {@code AuthDto} object containing the user's authentication credentials.
     * @return A {@link UserInfoDto} object representing the authenticated user information.
     * @throws AuthException Thrown when authentication fails due to bad credentials or due to a disabled account.
     */
    @Override
    public UserInfoDto authenticate(AuthDto authDto) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                authDto.email(),
                authDto.password()
        );
        Authentication authentication;
        try {
            authentication = authManager.authenticate(token);
        } catch (BadCredentialsException e) {
            throw AuthException.Code.BAD_CREDENTIALS.get();
        } catch (DisabledException e) {
            throw AuthException.Code.ACCOUNT_BLOCKED.get();
        }

        SecurityContext securityContext = holderStrategy.createEmptyContext();
        securityContext.setAuthentication(authentication);
        holderStrategy.setContext(securityContext);

        UserEntity user = (UserEntity) authentication.getPrincipal();

        user.setAccessToken(jwtService.generateToken(user, TokenType.ACCESS));
        user.setRefreshToken(jwtService.generateToken(user, TokenType.REFRESH));

        return userInfoMapper.toDto(user);
    }

    /**
     * Registers a new user based on the provided user information.
     * <p>
     * This method takes a {@link UserInfoDto} object containing user information and
     * converts it to a {@link UserEntity}. The converted user entity is then saved
     * using the user repository. Finally, the saved user entity is converted back to
     * a {@link UserInfoDto} object using {@link UserInfoMapper} and returned.
     *
     * @param userInfoDto A {@link UserInfoDto} object containing the user information to register.
     * @return A {@link UserInfoDto} object representing the registered user information.
     */
    @Override
    public UserInfoDto registerUser(UserInfoDto userInfoDto) {
        UserEntity user = userInfoMapper.toEntity(userInfoDto);
        return userInfoMapper.toDto(userRepository.save(user));
    }

    /**
     * Blocks a user with the specified email address.
     * <p>
     * This method retrieves the user entity associated with the provided email address
     * from the user repository. If the user is not found, a {@link NotFoundException} is
     * thrown indicating that the user was not found. If the user is found, their 'isEnabled'
     * property is set to false, indicating that the user is blocked. The updated user entity
     * is then saved using the user repository, and the updated user information is converted
     * to a {@link UserInfoDto} object using {@link UserInfoMapper} and returned.
     *
     * @param userEmail The email address of the user to be blocked.
     * @return A {@link UserInfoDto} object representing the updated user information after blocking.
     * @throws NotFoundException Thrown when the user with the specified email address is not found.
     */
    @Override
    public UserInfoDto blockUser(String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> NotFoundException.Code.USER_NOT_FOUND.get(userEmail));
        user.setIsEnabled(false);
        return userInfoMapper.toDto(userRepository.save(user));
    }

    /**
     * Unblocks a user with the specified email address.
     * <p>
     * This method retrieves the user entity associated with the provided email address
     * from the user repository. If the user is not found, a {@link NotFoundException} is
     * thrown indicating that the user was not found. If the user is found, their 'isEnabled'
     * property is set to true, indicating that the user is unblocked. The updated user entity
     * is then saved using the user repository, and the updated user information is converted
     * to a {@link UserInfoDto} object using {@link UserInfoMapper} and returned.
     *
     * @param userEmail The email address of the user to be unblocked.
     * @return A {@link UserInfoDto} object representing the updated user information after unblocking.
     * @throws NotFoundException Thrown when the user with the specified email address is not found.
     */
    @Override
    public UserInfoDto unblockUser(String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> NotFoundException.Code.USER_NOT_FOUND.get(userEmail));
        user.setIsEnabled(true);
        return userInfoMapper.toDto(userRepository.save(user));
    }
}
