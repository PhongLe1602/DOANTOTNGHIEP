package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.request.EnrollmentRequest;
import doan.ptit.programmingtrainingcenter.dto.response.StudentEnrollmentResponse;
import doan.ptit.programmingtrainingcenter.entity.Enrollment;
import doan.ptit.programmingtrainingcenter.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Boolean> checkEnrollment(@RequestParam String userId, @RequestParam String courseId) {
        boolean isEnrolled = enrollmentService.checkEnrollment(userId, courseId);
        return ResponseEntity.ok(isEnrolled);
    }

    @GetMapping("/students")
    public ResponseEntity<List<StudentEnrollmentResponse>> getAllStudentEnrollments() {
        return ResponseEntity.ok(enrollmentService.getAllStudentEnrollments());
    }
}
