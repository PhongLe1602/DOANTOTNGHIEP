package doan.ptit.programmingtrainingcenter.service.impl;


import doan.ptit.programmingtrainingcenter.dto.request.AttendanceRequest;
import doan.ptit.programmingtrainingcenter.dto.response.AttendanceSessionDetailResponse;
import doan.ptit.programmingtrainingcenter.dto.response.StudentAttendanceResponse;
import doan.ptit.programmingtrainingcenter.entity.*;
import doan.ptit.programmingtrainingcenter.exception.ConflictException;
import doan.ptit.programmingtrainingcenter.repository.*;
import doan.ptit.programmingtrainingcenter.service.AttendanceService;
import doan.ptit.programmingtrainingcenter.service.CourseClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private CourseClassRepository courseClassRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttendanceSessionRepository attendanceSessionRepository;

    @Autowired
    private ClassStudentRepository classStudentRepository;

    @Autowired
    private CourseClassService courseClassService;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Override
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    @Override
    public Attendance getAttendanceById(String id) {
        return attendanceRepository.findById(id).orElseThrow(() -> new RuntimeException("Attendance Not Found"));
    }



    @Override
    public void deleteAttendance(String id) {
        Attendance attendance = attendanceRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Attendance Not Found"));
        attendanceRepository.delete(attendance);
    }

    @Override
    public boolean checkIn(String sessionId, String studentId) {
        AttendanceSession session = attendanceSessionRepository.findById(sessionId).
                orElseThrow(() -> new RuntimeException("AttendanceSession Not Found"));
        User student = userRepository.findById(studentId).
                orElseThrow(() -> new RuntimeException("User Not Found"));
        CourseClass courseClass = courseClassRepository.findById(session.getCourseClass().getId()).
                orElseThrow(() -> new RuntimeException("CourseClass Not Found"));

        if (!classStudentRepository.existsByCourseClassIdAndStudentIdAndStatus(
                courseClass.getId(), studentId, ClassStudent.Status.STUDYING)) {
            throw new IllegalArgumentException("Bạn không phải học viên đang học của lớp này");
        }

        if (session.getExpiryTime().before(new Date())) {
            throw new IllegalArgumentException("Phiên điểm danh đã hết hạn");
        }


//        Attendance attendance = attendanceRepository.findBySession_IdAndStudent_Id(sessionId, studentId);
//        if (attendance != null) {
//            return false;
//        }
        if (attendanceRepository.existsBySession_IdAndStudent_Id(sessionId, studentId)) {
            throw new ConflictException("Bạn đã điểm danh rồi");
        }

        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(studentId,courseClass.getCourse().getId());
        // Lưu trạng thái điểm danh
        Attendance attendance = Attendance.builder()
                .session(session)
                .student(student)
                .status(Attendance.Status.PRESENT)
                .build();
//        courseClass.setCompletedSessions(courseClass.getCompletedSessions() + 1);
//        courseClassRepository.save(courseClass);
        System.out.println(enrollment.getCourse().getTitle());
        System.out.println(courseClass.getCompletedSessions());
        System.out.println(courseClass.getTotalSessions());
        BigDecimal progress = BigDecimal.valueOf((courseClass.getCompletedSessions()) / (double) courseClass.getTotalSessions());
        progress = progress.setScale(2, RoundingMode.HALF_UP);
        enrollment.setProgress(progress);
        enrollmentRepository.save(enrollment);
        attendanceRepository.save(attendance);

        return true;
    }

    @Override
    public List<Attendance> getAttendanceBySessionId(String sessionId) {
        return attendanceRepository.findBySessionId(sessionId);
    }

    @Override
    public List<Attendance> getAttendanceByStudentId(String studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }


}
