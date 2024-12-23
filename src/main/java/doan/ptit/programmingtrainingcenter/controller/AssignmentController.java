package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.request.AssignmentRequest;
import doan.ptit.programmingtrainingcenter.dto.response.ApiResponse;
import doan.ptit.programmingtrainingcenter.entity.Assignment;
import doan.ptit.programmingtrainingcenter.security.CustomUserDetails;
import doan.ptit.programmingtrainingcenter.service.AssignmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @GetMapping
    public List<Assignment> getAllAssignments() {
        return assignmentService.getAssignments();
    }

    @GetMapping("/{id}")
    public Assignment getAssignmentById(@PathVariable String id) {
        return assignmentService.getAssignmentById(id);
    }
    @PostMapping
    public ApiResponse<Assignment> createAssignment(@Valid @ModelAttribute AssignmentRequest assignmentRequest) throws IOException {

        Assignment createdAssignment = assignmentService.createAssignment(assignmentRequest);
        return ApiResponse.success("Giao bài thành công", createdAssignment);

    }
    @PutMapping("/{assignmentId}")
    public ResponseEntity<Assignment> updateAssignment(
            @PathVariable String assignmentId,
            @ModelAttribute AssignmentRequest assignmentRequest) throws IOException {
        Assignment updatedAssignment = assignmentService.updateAssignment( assignmentId ,assignmentRequest);
        return ResponseEntity.ok(updatedAssignment);
    }
    @DeleteMapping("/{assignmentId}")
    public ResponseEntity<String> deleteAssignment(@PathVariable String assignmentId) {
        assignmentService.deleteAssignment(assignmentId);
        return ResponseEntity.ok("Assignment deleted successfully");
    }
    @GetMapping("/student")
    public ResponseEntity<List<Assignment>> getAssignmentsByStudentId() {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Assignment> assignments = assignmentService.getAssignmentsOfStudent(currentUser.getId());
        return ResponseEntity.ok(assignments);
    }
    @GetMapping("/instructor")
    public ResponseEntity<List<Assignment>> getAssignments() {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Assignment> assignments = assignmentService.getAssignmentsOfInstructor(currentUser.getId());
        return ResponseEntity.ok(assignments);
    }
}
