package doan.ptit.programmingtrainingcenter.service.impl;


import doan.ptit.programmingtrainingcenter.dto.request.AttendanceRequest;
import doan.ptit.programmingtrainingcenter.dto.response.AttendanceSessionDetailResponse;
import doan.ptit.programmingtrainingcenter.dto.response.StudentAttendanceResponse;
import doan.ptit.programmingtrainingcenter.entity.Attendance;
import doan.ptit.programmingtrainingcenter.entity.AttendanceSession;
import doan.ptit.programmingtrainingcenter.entity.CourseClass;
import doan.ptit.programmingtrainingcenter.entity.User;
import doan.ptit.programmingtrainingcenter.repository.AttendanceRepository;
import doan.ptit.programmingtrainingcenter.repository.AttendanceSessionRepository;
import doan.ptit.programmingtrainingcenter.repository.CourseClassRepository;
import doan.ptit.programmingtrainingcenter.repository.UserRepository;
import doan.ptit.programmingtrainingcenter.service.AttendanceService;
import doan.ptit.programmingtrainingcenter.service.CourseClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private CourseClassService courseClassService;

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
    public String checkIn(String sessionId, String studentId) {
        AttendanceSession session = attendanceSessionRepository.findById(sessionId).
                orElseThrow(() -> new RuntimeException("AttendanceSession Not Found"));
        User student = userRepository.findById(studentId).
                orElseThrow(() -> new RuntimeException("User Not Found"));
        CourseClass courseClass = courseClassRepository.findById(session.getCourseClass().getId()).
                orElseThrow(() -> new RuntimeException("CourseClass Not Found"));
        if (session == null || session.getExpiryTime().before(new Date())) {
            return "Session invalid or expired!";
        }

        Attendance attendance = attendanceRepository.findBySession_IdAndStudent_Id(sessionId, studentId);
        if (attendance != null) {
            return "You have already checked in!";
        }

        // Lưu trạng thái điểm danh
        attendance = Attendance.builder()
                .session(session)
                .student(student)
                .status(Attendance.Status.PRESENT)
                .build();
        courseClass.setCompletedSessions(courseClass.getCompletedSessions() + 1);
        courseClassRepository.save(courseClass);
        attendanceRepository.save(attendance);

        return "Check-in successful!";
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
