package com.raymundo.farmtrack.service.impl;

import com.raymundo.farmtrack.dto.GradeDto;
import com.raymundo.farmtrack.entity.GradeEntity;
import com.raymundo.farmtrack.entity.UserEntity;
import com.raymundo.farmtrack.mapper.GradeMapper;
import com.raymundo.farmtrack.repository.GradeRepository;
import com.raymundo.farmtrack.repository.UserRepository;
import com.raymundo.farmtrack.service.GradeService;
import com.raymundo.farmtrack.util.exception.GradeException;
import com.raymundo.farmtrack.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link GradeService} interface for rating users and retrieving grades.
 * <p>
 * It utilizes a {@link GradeRepository} for accessing grade data, a {@link UserRepository}
 * for accessing user data, and a {@link GradeMapper} for mapping grade entities to DTOs.
 *
 * @author RaymundoZ
 */
@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;
    private final UserRepository userRepository;
    private final GradeMapper gradeMapper;


    /**
     * Rates a user based on the provided grade information.
     * <p>
     * This method retrieves the user entity associated with the provided email address
     * from the user repository. If the user is not found, a {@link NotFoundException} is
     * thrown indicating that the user was not found. The method checks if the current time
     * falls within the working hours (between 8:00 AM and 8:00 PM) and throws a
     * {@link GradeException} if it does not. Next, it checks if a grade for the user on
     * the current date exists in the grade repository. If a grade exists, it updates the
     * existing grade with the provided grade value; otherwise, it creates a new grade
     * entity based on the provided grade information. The grade entity is then saved
     * using the grade repository, and the updated or newly created grade information
     * is converted to a {@link GradeDto} object using {@link GradeMapper} and returned.
     *
     * @param gradeDto A {@link GradeDto} object containing the grade information.
     * @return A {@link GradeDto} object representing the rated user grade information.
     * @throws NotFoundException Thrown when the user with the specified email address is not found.
     * @throws GradeException    Thrown when the current time is not within working hours.
     */
    @Override
    public GradeDto rateUser(GradeDto gradeDto) {
        UserEntity user = userRepository.findByEmail(gradeDto.user())
                .orElseThrow(() -> NotFoundException.Code.USER_NOT_FOUND.get(gradeDto.user()));
        LocalTime time = LocalTime.now();
        LocalDate date = LocalDate.now();
        if (time.isBefore(LocalTime.of(20, 0, 0)) &&
                time.isAfter(LocalTime.of(8, 0, 0)))
            throw GradeException.Code.WORKING_DAY_NOT_ENDED.get();
        Optional<GradeEntity> optional = gradeRepository.findByCreatedDateAndUser(date, user);
        GradeEntity grade;
        if (optional.isPresent()) {
            grade = optional.get();
            grade.setGrade(gradeDto.grade());
        } else {
            grade = gradeMapper.toEntity(gradeDto);
            grade.setUser(user);
        }
        return gradeMapper.toDto(gradeRepository.save(grade));
    }

    /**
     * Retrieves a list of all grades.
     * <p>
     * This method retrieves all grade entities from the grade repository and maps them to
     * {@link GradeDto} objects using {@link GradeMapper}. The resulting list of grade DTOs
     * is returned.
     *
     * @return A list of {@link GradeDto} objects representing all grades.
     */
    @Override
    public List<GradeDto> getGrades() {
        return gradeRepository.findAll().stream()
                .map(gradeMapper::toDto)
                .toList();
    }

    /**
     * Retrieves a list of grades for a specific user.
     * <p>
     * This method retrieves the user entity associated with the provided email address
     * from the user repository. If the user is not found, a {@link NotFoundException} is
     * thrown indicating that the user was not found. Next, it retrieves all grade entities
     * associated with the user from the grade repository. The grade entities are then mapped
     * to {@link GradeDto} objects using {@link GradeMapper}, and the resulting list of grade
     * DTOs is returned.
     *
     * @param user The email address of the user for whom grades are to be retrieved.
     * @return A list of {@link GradeDto} objects representing grades for the specified user.
     * @throws NotFoundException Thrown when the user with the specified email address is not found.
     */
    @Override
    public List<GradeDto> getGradesByUser(String user) {
        UserEntity userEntity = userRepository.findByEmail(user)
                .orElseThrow(() -> NotFoundException.Code.USER_NOT_FOUND.get(user));
        return gradeRepository.findAllByUser(userEntity).stream()
                .map(gradeMapper::toDto)
                .toList();
    }
}
