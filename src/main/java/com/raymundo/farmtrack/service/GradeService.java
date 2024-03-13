package com.raymundo.farmtrack.service;

import com.raymundo.farmtrack.dto.GradeDto;

import java.util.List;

public interface GradeService {

    GradeDto rateUser(GradeDto gradeDto);

    List<GradeDto> getGrades();

    List<GradeDto> getGradesByUser(String user);
}
