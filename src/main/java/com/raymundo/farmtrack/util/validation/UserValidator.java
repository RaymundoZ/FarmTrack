package com.raymundo.farmtrack.util.validation;

import com.raymundo.farmtrack.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Custom validator for checking the uniqueness of user emails.
 * <p>
 * This class implements the {@link ConstraintValidator} interface to validate the uniqueness of user emails
 * annotated with the {@link UserEmailUnique} annotation. It checks if the provided email address does not
 * already exist in the database.
 *
 * @author RaymundoZ
 * @see UserEmailUnique
 * @see UserRepository
 */
@Component
@RequiredArgsConstructor
public class UserValidator implements ConstraintValidator<UserEmailUnique, String> {

    private final UserRepository userRepository;

    /**
     * Checks if the provided email address is unique.
     * <p>
     * This method validates the provided email address by checking if it exists in the database.
     * It returns true if the email address is unique (not found in the database), and false otherwise.
     *
     * @param value   The email address to be validated.
     * @param context The validation context.
     * @return True if the email address is unique, false otherwise.
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return userRepository.findByEmail(value).isEmpty();
    }
}
