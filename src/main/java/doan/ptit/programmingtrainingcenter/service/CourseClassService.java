package doan.ptit.programmingtrainingcenter.service;


import doan.ptit.programmingtrainingcenter.dto.request.ClassStudentRequest;
import doan.ptit.programmingtrainingcenter.dto.request.CourseClassRequest;
import doan.ptit.programmingtrainingcenter.entity.CourseClass;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CourseClassService {
    List<CourseClass> getAllClasses();
    CourseClass getClassById(String id);
    CourseClass createClass(CourseClassRequest courseClassRequest);
    CourseClass updateClass(String classId , CourseClassRequest courseClassRequest);
    void deleteClass(String id);
    List<CourseClass> getClassByCourseId(String courseId);


}
