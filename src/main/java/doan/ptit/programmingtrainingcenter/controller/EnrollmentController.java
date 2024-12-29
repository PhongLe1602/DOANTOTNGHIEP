package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.request.EnrollmentRequest;
import doan.ptit.programmingtrainingcenter.dto.response.PagedResponse;
import doan.ptit.programmingtrainingcenter.dto.response.StudentEnrollmentResponse;
import doan.ptit.programmingtrainingcenter.entity.Enrollment;
import doan.ptit.programmingtrainingcenter.security.CustomUserDetails;
import doan.ptit.programmingtrainingcenter.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping
    Enrollment addEnrollment(@RequestBody EnrollmentRequest enrollmentRequest) {
        return enrollmentService.addEnrollment(enrollmentRequest);
    }

    @GetMapping("/user/{userId}")
    List<Enrollment> getEnrollments(@PathVariable("userId") String userId) {
        return enrollmentService.getEnrollmentsByUser(userId);
    }
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkEnrollment( @RequestParam String courseId) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isEnrolled = enrollmentService.checkEnrollment(currentUser.getId(), courseId);
        return ResponseEntity.ok(isEnrolled);
    }

    @GetMapping("/students")
    public ResponseEntity<List<StudentEnrollmentResponse>> getAllStudentEnrollments() {
        return ResponseEntity.ok(enrollmentService.getAllStudentEnrollments());
    }
    @GetMapping("/user")
    public PagedResponse<Enrollment> getEnrollments(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "courseName", required = false) String courseName,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pageable pageable = PageRequest.of(page, size);
        Page<Enrollment> enrollmentPage = enrollmentService.getEnrollmentsStudent(currentUser.getId(), status, courseName, pageable);
        return new PagedResponse<>(enrollmentPage);
    }
}
