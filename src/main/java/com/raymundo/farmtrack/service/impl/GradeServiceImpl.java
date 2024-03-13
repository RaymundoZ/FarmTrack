package com.raymundo.farmtrack.service.impl;

import com.raymundo.farmtrack.dto.GradeDto;
import com.raymundo.farmtrack.entity.GradeEntity;
import com.raymundo.farmtrack.entity.UserEntity;
import com.raymundo.farmtrack.exception.GradeException;
import com.raymundo.farmtrack.exception.NotFoundException;
import com.raymundo.farmtrack.mapper.GradeMapper;
import com.raymundo.farmtrack.repository.GradeRepository;
import com.raymundo.farmtrack.repository.UserRepository;
import com.raymundo.farmtrack.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;
    private final UserRepository userRepository;
    private final GradeMapper gradeMapper;

    @Override
    public GradeDto rateUser(GradeDto gradeDto) {
        UserEntity user = userRepository.findByEmail(gradeDto.user())
                .orElseThrow(() -> NotFoundException.Code.USER_NOT_FOUND.get(gradeDto.user()));
        LocalTime time = LocalTime.now();
        LocalDate date = LocalDate.now();
        if (time.isBefore(LocalTime.of(20, 0, 0)) &&
                time.isAfter(LocalTime.of(8, 0, 0)))
            throw GradeException.Code.WORKING_DAY_NOT_ENDED.get();
        if (gradeRepository.findByCreatedDateAndUser(date, user).isPresent())
            throw GradeException.Code.GRADE_ALREADY_SET.get();

        GradeEntity grade = gradeMapper.toEntity(gradeDto);
        grade.setUser(user);
        return gradeMapper.toDto(gradeRepository.save(grade));
    }

    @Override
    public List<GradeDto> getGrades() {
        return gradeRepository.findAll().stream()
                .map(gradeMapper::toDto)
                .toList();
    }

    @Override
    public List<GradeDto> getGradesByUser(String user) {
        UserEntity userEntity = userRepository.findByEmail(user)
                .orElseThrow(() -> NotFoundException.Code.USER_NOT_FOUND.get(user));
        return gradeRepository.findAllByUser(userEntity).stream()
                .map(gradeMapper::toDto)
                .toList();
    }
}
