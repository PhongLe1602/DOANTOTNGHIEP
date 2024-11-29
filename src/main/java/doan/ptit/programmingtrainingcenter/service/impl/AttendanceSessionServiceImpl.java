package doan.ptit.programmingtrainingcenter.service.impl;


import doan.ptit.programmingtrainingcenter.dto.request.AttendanceSessionRequest;
import doan.ptit.programmingtrainingcenter.entity.AttendanceSession;
import doan.ptit.programmingtrainingcenter.entity.CourseClass;
import doan.ptit.programmingtrainingcenter.entity.User;
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
import java.util.UUID;

@Service
public class AttendanceSessionServiceImpl implements AttendanceSessionService {

    @Autowired
    private AttendanceSessionRepository attendanceSessionRepository;

    @Autowired
    private CourseClassRepository courseClassRepository;

    @Autowired
    private UserRepository userRepository;

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

        return attendanceSessionRepository.save(savedSession);
    }

}
