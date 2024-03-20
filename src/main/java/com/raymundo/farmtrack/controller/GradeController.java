package com.raymundo.farmtrack.controller;

import com.raymundo.farmtrack.dto.GradeDto;
import com.raymundo.farmtrack.dto.basic.SuccessDto;
import com.raymundo.farmtrack.service.GradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class that handles grade-related operations.
 * <p>
 * This class provides REST endpoints for rating users and retrieving grades.
 * It utilizes the {@link GradeService} to perform these operations.
 *
 * @author RaymundoZ
 */
@Tag(name = "GradeController", description = "Controller class that handles grade-related operations")
@RestController
@RequestMapping(value = "/grade")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    /**
     * Endpoint for rating a user.
     * <p>
     * This method handles the rating of a user by accepting a POST request with a JSON body
     * containing the rating information in the form of {@link GradeDto}. The rating information
     * is validated using the {@link Valid} annotation. If the rating is successfully created
     * by the {@link GradeService}, the method returns a {@link ResponseEntity} with a success message
     * and the created grade information in {@link GradeDto} format, along with an HTTP status code
     * 201 (CREATED).
     *
     * @param gradeDto A {@link GradeDto} object containing the rating information.
     * @return A {@link ResponseEntity} containing a success message and the created grade information upon successful rating.
     */
    @Operation(summary = "Endpoint for rating a user")
    @PostMapping
    public ResponseEntity<SuccessDto<GradeDto>> rateUser(@Valid @RequestBody GradeDto gradeDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessDto<>(
                        HttpStatus.CREATED.value(),
                        "Grade successfully created",
                        gradeService.rateUser(gradeDto)
                ));
    }

    /**
     * Endpoint for retrieving all grades.
     * <p>
     * This method handles the retrieval of all grades by accepting a GET request.
     * It retrieves all grades using the {@link GradeService} and returns a {@link ResponseEntity}
     * with a success message and a list of all grades in {@link GradeDto} format, along with
     * an HTTP status code 200 (OK).
     *
     * @return A {@link ResponseEntity} containing a success message and a list of all grades upon successful retrieval.
     */
    @Operation(summary = "Endpoint for retrieving all grades")
    @GetMapping
    public ResponseEntity<SuccessDto<List<GradeDto>>> getGrades() {
        return ResponseEntity.ok(new SuccessDto<>(
                HttpStatus.OK.value(),
                "All grades successfully received",
                gradeService.getGrades()
        ));
    }

    /**
     * Endpoint for retrieving grades by user.
     * <p>
     * This method handles the retrieval of grades for a specific user by accepting a GET request
     * with a path variable specifying the user's email address. It retrieves grades for the specified
     * user using the {@link GradeService} and returns a {@link ResponseEntity} with a success message
     * and a list of grades for the user in {@link GradeDto} format, along with an HTTP status code 200 (OK).
     *
     * @param userEmail The email address of the user for whom grades are to be retrieved.
     * @return A {@link ResponseEntity} containing a success message and a list of grades for the user upon successful retrieval.
     */
    @Operation(summary = "Endpoint for retrieving grades by user")
    @GetMapping(value = "/{userEmail}")
    public ResponseEntity<SuccessDto<List<GradeDto>>> getGradesByUser(@PathVariable
                                                                      @Parameter(description = "The email address of the user for whom grades are to be retrieved")
                                                                      String userEmail) {
        return ResponseEntity.ok(new SuccessDto<>(
                HttpStatus.OK.value(),
                "All grades successfully received",
                gradeService.getGradesByUser(userEmail)
        ));
    }
}
