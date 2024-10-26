package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.request.EnrollmentRequest;
import doan.ptit.programmingtrainingcenter.entity.Enrollment;
import doan.ptit.programmingtrainingcenter.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
