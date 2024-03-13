package com.raymundo.farmtrack.controller;

import com.raymundo.farmtrack.dto.GradeDto;
import com.raymundo.farmtrack.dto.basic.SuccessDto;
import com.raymundo.farmtrack.service.GradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/grade")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    @PostMapping
    public ResponseEntity<SuccessDto<GradeDto>> rateUser(@Valid @RequestBody GradeDto gradeDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessDto<>(
                        HttpStatus.CREATED.value(),
                        "Grade successfully created",
                        gradeService.rateUser(gradeDto)
                ));
    }

    @GetMapping
    public ResponseEntity<SuccessDto<List<GradeDto>>> getGrades() {
        return ResponseEntity.ok(new SuccessDto<>(
                HttpStatus.OK.value(),
                "All grades successfully recieved",
                gradeService.getGrades()
        ));
    }

    @GetMapping(value = "{userEmail}")
    public ResponseEntity<SuccessDto<List<GradeDto>>> getGradesByUser(@PathVariable String userEmail) {
        return ResponseEntity.ok(new SuccessDto<>(
                HttpStatus.OK.value(),
                "All grades successfully recieved",
                gradeService.getGradesByUser(userEmail)
        ));
    }
}
