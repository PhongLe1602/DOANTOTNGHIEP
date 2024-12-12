package doan.ptit.programmingtrainingcenter.service.impl;


import doan.ptit.programmingtrainingcenter.dto.request.EnrollmentRequest;
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

}
