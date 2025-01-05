package doan.ptit.programmingtrainingcenter.service.impl;


import doan.ptit.programmingtrainingcenter.dto.request.AttendanceSessionRequest;
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
import doan.ptit.programmingtrainingcenter.service.AttendanceSessionService;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.SecondaryRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AttendanceSessionServiceImpl implements AttendanceSessionService {

    @Autowired
    private AttendanceSessionRepository attendanceSessionRepository;

    @Autowired
    private CourseClassRepository courseClassRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Value("${app.frontend.url}")
    private String fondEndUrl;


    @Transactional
    @Override
    public AttendanceSession createSession(AttendanceSessionRequest attendanceSessionRequest, String instructorId) {
        Date now = new Date();
        Date expiryTime = new Date(now.getTime() + (long) attendanceSessionRequest.getDurationMinutes() * 60 * 1000);

        CourseClass courseClass = courseClassRepository.findById(attendanceSessionRequest.getClassId())
                .orElseThrow(() -> new RuntimeException("CourseClass Not Found"));
        User instructor = userRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        AttendanceSession session = AttendanceSession.builder()
                .courseClass(courseClass)
                .instructor(instructor)
                .createdAt(now)
                .expiryTime(expiryTime)
                .build();

        AttendanceSession savedSession = attendanceSessionRepository.save(session);

        String qrContent = fondEndUrl + "/checkin?sessionId=" + savedSession.getId();
        savedSession.setQrContent(qrContent);
        courseClass.setCompletedSessions(courseClass.getCompletedSessions() + 1);
        courseClassRepository.save(courseClass);

        return attendanceSessionRepository.save(savedSession);
    }
    @Override
    public AttendanceSessionDetailResponse getAttendanceSessionDetail(String sessionId) {
        // Lấy thông tin phiên điểm danh
        AttendanceSession session = attendanceSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Attendance Session Not Found"));

        //Danh sach hoc sinh trong lop
        List<User> classStudents = courseClassRepository.findUsersByClassId(session.getCourseClass().getId());

        // Lấy danh sách điểm danh của phiên này
        List<Attendance> attendances = attendanceRepository.findBySessionId(sessionId);

        // Tách học sinh đã và chưa điểm danh
        List<User> attendedStudents = attendances.stream()
                .map(Attendance::getStudent)
                .distinct()
                .toList();

        List<User> notAttendedStudents = classStudents.stream()
                .filter(student -> !attendedStudents.contains(student))
                .toList();

        // Tách học sinh đã và chưa điểm danh
        return AttendanceSessionDetailResponse.builder()
                .sessionId(session.getId())
                .className(session.getCourseClass().getName())
                .classId(session.getCourseClass().getId())
                .startTime(session.getCreatedAt())
                .duration(calculateDurationMinutes(session.getCreatedAt(), session.getExpiryTime()))
                .attendedStudents(attendedStudents.stream()
                        .map(student -> StudentAttendanceResponse.builder()
                                .id(student.getId())
                                .name(student.getFullName())
                                .attendanceTime(findAttendanceTime(attendances, student))
                                .build())
                        .collect(Collectors.toList()))
                .notAttendedStudents(notAttendedStudents.stream()
                        .map(student -> StudentAttendanceResponse.builder()
                                .id(student.getId())
                                .name(student.getFullName())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public List<AttendanceSession> getAttendanceSessionsOfClass(String classId) {
        return attendanceSessionRepository.findByCourseClassId(classId);
    }

    // Calculate session duration in minutes
    private long calculateDurationMinutes(Date startTime, Date expiryTime) {
        return (expiryTime.getTime() - startTime.getTime()) / (60 * 1000);
    }

    // Find attendance time for a student
    private Date findAttendanceTime(List<Attendance> attendances, User student) {
        return attendances.stream()
                .filter(a -> a.getStudent().getId().equals(student.getId()))
                .findFirst()
                .map(Attendance::getAttendanceDate)
                .orElse(null);
    }


}
