package doan.ptit.programmingtrainingcenter.service.impl;


import doan.ptit.programmingtrainingcenter.dto.request.ClassStudentRequest;
import doan.ptit.programmingtrainingcenter.dto.request.CourseClassRequest;
import doan.ptit.programmingtrainingcenter.entity.*;
import doan.ptit.programmingtrainingcenter.mapper.CourseClassMapper;
import doan.ptit.programmingtrainingcenter.repository.*;
import doan.ptit.programmingtrainingcenter.service.CourseClassService;
import doan.ptit.programmingtrainingcenter.service.EnrollmentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class CourseClassServiceImpl implements CourseClassService {

    @Autowired
    private CourseClassRepository classRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseClassMapper courseClassMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;




    @Override
    public List<CourseClass> getAllClasses() {
        return classRepository.findAll();
    }

    @Override
    public CourseClass getClassById(String id) {
        return classRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Class Not Found"));
    }

    @Override
    public CourseClass createClass(CourseClassRequest courseClassRequest) {
        Course course = courseRepository.findById(courseClassRequest.getCourseId()).
                orElseThrow(() -> new RuntimeException("Courses Not Found"));
        User user = userRepository.findById(courseClassRequest.getInstructorId()).
                orElseThrow(() -> new RuntimeException("User Not Found"));
        CourseClass newClass = courseClassMapper.toClass(courseClassRequest, course ,user);

        return classRepository.save(newClass);
    }

    @Override
    public CourseClass updateClass(String classId , CourseClassRequest courseClassRequest) {
        Course course = courseRepository.findById(courseClassRequest.getCourseId()).
                orElseThrow(() -> new RuntimeException("Courses Not Found"));
        CourseClass classToUpdate = classRepository.findById(classId).
                orElseThrow(() -> new RuntimeException("Class Not Found"));
        User user = userRepository.findById(courseClassRequest.getInstructorId()).
                orElseThrow(() -> new RuntimeException("User Not Found"));
        courseClassMapper.updateClass(classToUpdate,courseClassRequest,course,user);

        return classRepository.save(classToUpdate);
    }

    @Override
    public void deleteClass(String id) {
        classRepository.deleteById(id);
    }

    @Override
    public List<CourseClass> getClassByCourseId(String courseId) {
        return classRepository.findAllByCourseId(courseId);
    }

    @Override
    public List<User> getStudentsByClassId(String classId) {
        return classRepository.findUsersByClassId(classId);
    }

    @Override
    public List<CourseClass> getClassByInstructorId(String instructorId) {
        return classRepository.findByInstructorId(instructorId);
    }

    @Override
    public List<CourseClass> getClassesByStudentId(String studentId) {
        return classRepository.findClassesByStudentId(studentId);
    }

    @Override
    public List<CourseClass> getClassByCourse(String courseId, String userId) {
        // Tìm kiếm đăng ký của người dùng với khóa học
        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(userId, courseId);

        // Kiểm tra nếu không tìm thấy đăng ký
        if (enrollment == null) {
            throw new EntityNotFoundException( "Enrollment not found");
        }

        // Định nghĩa một Map ánh xạ trạng thái với thông báo
        Map<String, String> statusMessages = Map.of(
                "PENDING", "Khóa học chưa được kích hoạt",
                "STUDYING", "Bạn đã chọn lớp học này"
        );

        // Kiểm tra trạng thái và xử lý
        String status = String.valueOf(enrollment.getStatus());
        if (statusMessages.containsKey(status)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, statusMessages.get(status));
        }

        if ("ACTIVE".equals(status)) {
            return classRepository.findAllByCourseId(courseId);
        }

        // Trạng thái không hợp lệ
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trạng thái không hợp lệ");
    }



}
