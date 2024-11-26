package doan.ptit.programmingtrainingcenter.service.impl;


import doan.ptit.programmingtrainingcenter.dto.request.ClassStudentRequest;
import doan.ptit.programmingtrainingcenter.entity.ClassStudent;
import doan.ptit.programmingtrainingcenter.entity.CourseClass;
import doan.ptit.programmingtrainingcenter.entity.User;
import doan.ptit.programmingtrainingcenter.repository.ClassStudentRepository;
import doan.ptit.programmingtrainingcenter.repository.CourseClassRepository;
import doan.ptit.programmingtrainingcenter.repository.UserRepository;
import doan.ptit.programmingtrainingcenter.service.ClassStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ClassStudentServiceImpl implements ClassStudentService {

    @Autowired
    private ClassStudentRepository classStudentRepository;

    @Autowired
    private CourseClassRepository courseClassRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ClassStudent addUsertoClassStudent(String userId, ClassStudentRequest classStudentRequest) {
        ClassStudent classStudent = new ClassStudent();
        CourseClass courseClass = courseClassRepository.findById(classStudentRequest.getClassId()).
                orElseThrow(() -> new RuntimeException("Class Not Found"));
        User user = userRepository.findById(userId).
                orElseThrow(() -> new RuntimeException("User Not Found"));

        classStudent.setCourseClass(courseClass);
        classStudent.setStudent(user);
        classStudent.setStatus(ClassStudent.Status.valueOf("STUDYING"));
        classStudent.setJoinedDate(new Date());

        return  classStudentRepository.save(classStudent);
    }
}
