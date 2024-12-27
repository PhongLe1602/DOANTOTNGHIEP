package doan.ptit.programmingtrainingcenter.service;


import doan.ptit.programmingtrainingcenter.dto.request.EnrollmentRequest;
import doan.ptit.programmingtrainingcenter.dto.response.StudentEnrollmentResponse;
import doan.ptit.programmingtrainingcenter.entity.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface EnrollmentService {
    Enrollment addEnrollment(EnrollmentRequest enrollmentRequest);
    List<Enrollment> getEnrollments();
    List<Enrollment> getEnrollmentsByUser(String userId);
    boolean checkEnrollment(String userId, String courseId);
    List<StudentEnrollmentResponse> getAllStudentEnrollments();
    long getStudyingCount();
    long getCompletedCount();
    long getPendingCount();
    long getTotalStudentsEnrolled();
    boolean checkEnrollmentActive(String userId , String courseId);
    Page<Enrollment> getEnrollmentsStudent(String userId, String status, String courseName, Pageable pageable);


}
