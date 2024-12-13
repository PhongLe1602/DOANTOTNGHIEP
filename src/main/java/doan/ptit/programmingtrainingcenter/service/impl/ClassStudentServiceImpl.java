package doan.ptit.programmingtrainingcenter.service.impl;


import doan.ptit.programmingtrainingcenter.dto.request.ClassStudentRequest;
import doan.ptit.programmingtrainingcenter.entity.ClassStudent;
import doan.ptit.programmingtrainingcenter.entity.CourseClass;
import doan.ptit.programmingtrainingcenter.entity.Enrollment;
import doan.ptit.programmingtrainingcenter.entity.User;
import doan.ptit.programmingtrainingcenter.repository.ClassStudentRepository;
import doan.ptit.programmingtrainingcenter.repository.CourseClassRepository;
import doan.ptit.programmingtrainingcenter.repository.EnrollmentRepository;
import doan.ptit.programmingtrainingcenter.repository.UserRepository;
import doan.ptit.programmingtrainingcenter.service.ClassStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ClassStudentServiceImpl implements ClassStudentService {

    @Autowired
    private ClassStudentRepository classStudentRepository;

    @Autowired
    private CourseClassRepository courseClassRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Override
    public ClassStudent addUsertoClassStudent(String userId, ClassStudentRequest classStudentRequest) {
        ClassStudent classStudent = new ClassStudent();
        CourseClass courseClass = courseClassRepository.findById(classStudentRequest.getClassId()).
                orElseThrow(() -> new RuntimeException("Class Not Found"));
        User user = userRepository.findById(userId).
                orElseThrow(() -> new RuntimeException("User Not Found"));

        Optional<ClassStudent> existingClassStudent = classStudentRepository.findByCourseClassIdAndStudentId(
                classStudentRequest.getClassId(), userId);

        if (existingClassStudent.isPresent()) {
            throw new RuntimeException("Student already enrolled in this class");
        }


        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(userId, courseClass.getCourse().getId());

        if (enrollment == null) {
            throw new RuntimeException("User has not enrolled in the course");
        }


        if (!Enrollment.Status.ACTIVE.equals(enrollment.getStatus())) {
            throw new RuntimeException("User's enrollment is not active");
        }

        classStudent.setCourseClass(courseClass);
        classStudent.setStudent(user);
        classStudent.setStatus(ClassStudent.Status.valueOf("STUDYING"));
        classStudent.setJoinedDate(new Date());

//        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(userId, courseClass.getCourse().getId());
        enrollment.setStatus(Enrollment.Status.valueOf("STUDYING"));
        enrollmentRepository.save(enrollment);

        return  classStudentRepository.save(classStudent);
    }
}
