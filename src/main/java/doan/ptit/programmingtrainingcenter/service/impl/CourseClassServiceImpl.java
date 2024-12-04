package doan.ptit.programmingtrainingcenter.service.impl;


import doan.ptit.programmingtrainingcenter.dto.request.ClassStudentRequest;
import doan.ptit.programmingtrainingcenter.dto.request.CourseClassRequest;
import doan.ptit.programmingtrainingcenter.entity.ClassStudent;
import doan.ptit.programmingtrainingcenter.entity.CourseClass;
import doan.ptit.programmingtrainingcenter.entity.Course;
import doan.ptit.programmingtrainingcenter.entity.User;
import doan.ptit.programmingtrainingcenter.mapper.CourseClassMapper;
import doan.ptit.programmingtrainingcenter.repository.ClassStudentRepository;
import doan.ptit.programmingtrainingcenter.repository.CourseClassRepository;
import doan.ptit.programmingtrainingcenter.repository.CourseRepository;
import doan.ptit.programmingtrainingcenter.repository.UserRepository;
import doan.ptit.programmingtrainingcenter.service.CourseClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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


}
