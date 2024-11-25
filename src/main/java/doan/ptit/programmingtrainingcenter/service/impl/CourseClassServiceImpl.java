package doan.ptit.programmingtrainingcenter.service.impl;


import doan.ptit.programmingtrainingcenter.dto.request.CourseClassRequest;
import doan.ptit.programmingtrainingcenter.entity.CourseClass;
import doan.ptit.programmingtrainingcenter.entity.Course;
import doan.ptit.programmingtrainingcenter.mapper.CourseClassMapper;
import doan.ptit.programmingtrainingcenter.repository.CourseClassRepository;
import doan.ptit.programmingtrainingcenter.repository.CourseRepository;
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
        CourseClass newClass = courseClassMapper.toClass(courseClassRequest, course);

        return classRepository.save(newClass);
    }

    @Override
    public CourseClass updateClass(String classId , CourseClassRequest courseClassRequest) {
        Course course = courseRepository.findById(courseClassRequest.getCourseId()).
                orElseThrow(() -> new RuntimeException("Courses Not Found"));
        CourseClass classToUpdate = classRepository.findById(classId).
                orElseThrow(() -> new RuntimeException("Class Not Found"));
        courseClassMapper.updateClass(classToUpdate,courseClassRequest,course);

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
}
