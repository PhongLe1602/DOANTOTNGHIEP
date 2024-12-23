package doan.ptit.programmingtrainingcenter.service.impl;


import doan.ptit.programmingtrainingcenter.dto.request.EnrollmentRequest;
import doan.ptit.programmingtrainingcenter.dto.response.CoursesResponse;
import doan.ptit.programmingtrainingcenter.dto.response.EnrollmentResponse;
import doan.ptit.programmingtrainingcenter.dto.response.StudentEnrollmentResponse;
import doan.ptit.programmingtrainingcenter.dto.response.UserResponse;
import doan.ptit.programmingtrainingcenter.entity.Course;
import doan.ptit.programmingtrainingcenter.entity.Enrollment;
import doan.ptit.programmingtrainingcenter.entity.User;
import doan.ptit.programmingtrainingcenter.mapper.EnrollmentMapper;
import doan.ptit.programmingtrainingcenter.repository.CourseRepository;
import doan.ptit.programmingtrainingcenter.repository.EnrollmentRepository;
import doan.ptit.programmingtrainingcenter.repository.UserRepository;
import doan.ptit.programmingtrainingcenter.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private EnrollmentMapper enrollmentMapper;

    @Override
    public Enrollment addEnrollment(EnrollmentRequest enrollmentRequest) {

        User user = userRepository.findById(enrollmentRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Course course = courseRepository.findById(enrollmentRequest.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Enrollment enrollment = enrollmentMapper.toEntity(enrollmentRequest, user, course);

        return enrollmentRepository.save(enrollment);
    }


    @Override
    public List<Enrollment> getEnrollments() {
        return enrollmentRepository.findAll();
    }

    @Override
    public List<Enrollment> getEnrollmentsByUser(String userId) {
        return enrollmentRepository.findByUserId(userId);
    }

    @Override
    public boolean checkEnrollment(String userId, String courseId) {
        List<Enrollment> enrollments = enrollmentRepository.findByUserId(userId);
        return enrollments.stream()
                .anyMatch(enrollment -> enrollment.getCourse().getId().equals(courseId));
    }

    @Override
    public List<StudentEnrollmentResponse> getAllStudentEnrollments() {
        // Step 1: Get all distinct users who have enrollments
        List<User> users = enrollmentRepository.findDistinctUsersWithEnrollments();

        // Step 2: For each user, get their enrollments and map to response
        return users.stream().map(user -> {
            // Get all enrollments for current user
            List<Enrollment> userEnrollments = enrollmentRepository.findEnrollmentDetailsByUserId(user.getId());

            // Map enrollments to EnrollmentResponse
            List<EnrollmentResponse> enrollmentResponses = userEnrollments.stream()
                    .map(this::mapToEnrollmentResponse)
                    .collect(Collectors.toList());

            // Build StudentEnrollmentResponse
            return StudentEnrollmentResponse.builder()
                    .id(user.getId())
                    .name(user.getFullName())
                    .email(user.getEmail())
                    .phone(user.getPhoneNumber())
                    .totalEnrollments(enrollmentResponses.size())
                    .enrollments(enrollmentResponses)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public long getStudyingCount() {
        return enrollmentRepository.countByStatus(Enrollment.Status.STUDYING);
    }


    @Override
    public long getCompletedCount() {
        return enrollmentRepository.countByStatus(Enrollment.Status.COMPLETED);
    }

    @Override
    public long getPendingCount() {
        return enrollmentRepository.countByStatus(Enrollment.Status.PENDING);
    }

    @Override
    public long getTotalStudentsEnrolled() {
        return enrollmentRepository.countDistinctUserIdFromEnrollments();
    }

    private EnrollmentResponse mapToEnrollmentResponse(Enrollment enrollment) {
        return EnrollmentResponse.builder()
                .id(enrollment.getId())
                .courses(mapToCoursesResponse(enrollment.getCourse()))
                .orderItem(enrollment.getOrderItem())
                .enrollmentDate(enrollment.getEnrollmentDate())
                .status(enrollment.getStatus())
                .progress(enrollment.getProgress())
                .lastAccessed(enrollment.getLastAccessed())
                .build();
    }



    private CoursesResponse mapToCoursesResponse(Course course) {
        return CoursesResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                // Add other course fields as needed
                .build();
    }

}
