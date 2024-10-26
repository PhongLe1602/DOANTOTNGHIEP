package doan.ptit.programmingtrainingcenter.service.impl;


import doan.ptit.programmingtrainingcenter.dto.request.EnrollmentRequest;
import doan.ptit.programmingtrainingcenter.entity.Course;
import doan.ptit.programmingtrainingcenter.entity.Enrollment;
import doan.ptit.programmingtrainingcenter.entity.User;
import doan.ptit.programmingtrainingcenter.mapper.EnrollmentMapper;
import doan.ptit.programmingtrainingcenter.repository.CourseRepository;
import doan.ptit.programmingtrainingcenter.repository.EnrollmentRepository;
import doan.ptit.programmingtrainingcenter.repository.UserRepository;
import doan.ptit.programmingtrainingcenter.service.CourseService;
import doan.ptit.programmingtrainingcenter.service.EnrollmentService;
import doan.ptit.programmingtrainingcenter.service.UserService;
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
        // Tìm kiếm đối tượng User dựa trên userId
        User user = userRepository.findById(enrollmentRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found")); // Xử lý lỗi nếu không tìm thấy User

        // Tìm kiếm đối tượng Course dựa trên courseId
        Course course = courseRepository.findById(enrollmentRequest.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found")); // Xử lý lỗi nếu không tìm thấy Course

        // Sử dụng mapper để chuyển đổi EnrollmentRequest thành Enrollment
        Enrollment enrollment = enrollmentMapper.toEntity(enrollmentRequest, user, course);

        // Lưu đối tượng Enrollment vào cơ sở dữ liệu
        return enrollmentRepository.save(enrollment); // Trả về Enrollment đã được lưu
    }


    @Override
    public List<Enrollment> getEnrollments() {
        return List.of();
    }

    @Override
    public List<Enrollment> getEnrollmentsByUser(String userId) {
        return enrollmentRepository.findByUserId(userId);
    }
}
