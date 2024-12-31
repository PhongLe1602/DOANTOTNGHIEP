package doan.ptit.programmingtrainingcenter.service.impl;


import doan.ptit.programmingtrainingcenter.dto.request.ClassStudentRequest;
import doan.ptit.programmingtrainingcenter.entity.*;
import doan.ptit.programmingtrainingcenter.exception.ConflictException;
import doan.ptit.programmingtrainingcenter.repository.*;
import doan.ptit.programmingtrainingcenter.service.ClassStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
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

    @Autowired
    private ScheduleRepository scheduleRepository;

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
        // Lấy tất cả các lớp mà học viên đang tham gia với trạng thái 'STUDYING'
        List<ClassStudent> enrolledClasses = classStudentRepository.findByStudentIdAndStatus(userId, ClassStudent.Status.STUDYING);

        // Kiểm tra lịch học của từng lớp học mà học viên đang tham gia
        for (ClassStudent enrolledClass : enrolledClasses) {
            // Lấy lịch học của lớp hiện tại
            List<Schedule> existingSchedules = scheduleRepository.findByCourseClassId(enrolledClass.getCourseClass().getId());

            for (Schedule existingSchedule : existingSchedules) {
                // So sánh lịch học của lớp mới với các lớp mà học viên đã tham gia
                if (isTimeConflict(existingSchedule, courseClass)) {
                    throw new ConflictException("Bị trùng lịch học");
                }
            }
        }

        classStudent.setCourseClass(courseClass);
        classStudent.setStudent(user);
        classStudent.setStatus(ClassStudent.Status.valueOf("STUDYING"));
        classStudent.setJoinedDate(new Date());
        courseClass.setCurrentStudentCount(courseClass.getCurrentStudentCount() + 1);

//        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(userId, courseClass.getCourse().getId());
        enrollment.setStatus(Enrollment.Status.valueOf("STUDYING"));
        enrollmentRepository.save(enrollment);
        courseClassRepository.save(courseClass);

        return  classStudentRepository.save(classStudent);
    }

    private boolean isTimeConflict(Schedule existingSchedule, CourseClass courseClass) {
        // Kiểm tra xem thời gian của lớp học mới có trùng với lịch học cũ không
        List<Schedule> courseClassSchedules = scheduleRepository.findByCourseClassId(courseClass.getId());
        for (Schedule schedule : courseClassSchedules) {
            // So sánh thời gian bắt đầu và kết thúc của các buổi học
            if ((existingSchedule.getStartTime().before(schedule.getEndTime()) && existingSchedule.getEndTime().after(schedule.getStartTime()))) {
                return true; // Trùng lịch
            }
        }
        return false;
    }
}
