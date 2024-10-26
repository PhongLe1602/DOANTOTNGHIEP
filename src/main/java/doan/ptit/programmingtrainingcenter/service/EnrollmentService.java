package doan.ptit.programmingtrainingcenter.service;


import doan.ptit.programmingtrainingcenter.dto.request.EnrollmentRequest;
import doan.ptit.programmingtrainingcenter.entity.Enrollment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EnrollmentService {
    Enrollment addEnrollment(EnrollmentRequest enrollmentRequest);
    List<Enrollment> getEnrollments();
    List<Enrollment> getEnrollmentsByUser(String userId);
}
