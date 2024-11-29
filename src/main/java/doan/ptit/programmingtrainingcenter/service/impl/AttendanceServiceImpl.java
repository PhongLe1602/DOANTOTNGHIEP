package doan.ptit.programmingtrainingcenter.service.impl;


import doan.ptit.programmingtrainingcenter.dto.request.AttendanceRequest;
import doan.ptit.programmingtrainingcenter.entity.Attendance;
import doan.ptit.programmingtrainingcenter.entity.AttendanceSession;
import doan.ptit.programmingtrainingcenter.entity.CourseClass;
import doan.ptit.programmingtrainingcenter.entity.User;
import doan.ptit.programmingtrainingcenter.repository.AttendanceRepository;
import doan.ptit.programmingtrainingcenter.repository.AttendanceSessionRepository;
import doan.ptit.programmingtrainingcenter.repository.CourseClassRepository;
import doan.ptit.programmingtrainingcenter.repository.UserRepository;
import doan.ptit.programmingtrainingcenter.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

    @Override
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    @Override
    public Attendance getAttendanceById(String id) {
        return attendanceRepository.findById(id).orElseThrow(() -> new RuntimeException("Attendance Not Found"));
    }

//    @Override
//    public Attendance addAttendance(AttendanceRequest attendanceRequest) {
//        Attendance attendance = new Attendance();
//        CourseClass courseClass = courseClassRepository.findById(attendanceRequest.getClassId()).
//                orElseThrow(() -> new RuntimeException("CourseClass Not Found"));
//        User student = userRepository.findById(attendanceRequest.getStudentId()).
//                orElseThrow(() -> new RuntimeException("User Not Found"));
//        User createBy = userRepository.findById(attendanceRequest.getCreatedBy()).
//                orElseThrow(() -> new RuntimeException("User Not Found"));
//
//        attendance.setStudent(student);
//        attendance.setClassEntity(courseClass);
//        attendance.setCreatedBy(createBy);
//        attendance.setDate(attendanceRequest.getDate());
//        attendance.setStatus(Attendance.Status.valueOf(attendanceRequest.getStatus()));
//        attendance.setNote(attendanceRequest.getNote());
//        return attendanceRepository.save(attendance);
//    }
//
//    @Override
//    public Attendance updateAttendance(String id ,AttendanceRequest attendanceRequest) {
//        Attendance attendance = attendanceRepository.findById(id).
//                orElseThrow(() -> new RuntimeException("Attendance Not Found"));
//        CourseClass courseClass = courseClassRepository.findById(attendanceRequest.getClassId()).
//                orElseThrow(() -> new RuntimeException("CourseClass Not Found"));
//        User student = userRepository.findById(attendanceRequest.getStudentId()).
//                orElseThrow(() -> new RuntimeException("User Not Found"));
//        User createBy = userRepository.findById(attendanceRequest.getCreatedBy()).
//                orElseThrow(() -> new RuntimeException("User Not Found"));
//        attendance.setNote(attendanceRequest.getNote());
//        attendance.setDate(attendanceRequest.getDate());
//        attendance.setStatus(Attendance.Status.valueOf(attendanceRequest.getStatus()));
//        attendance.setStudent(student);
//        attendance.setClassEntity(courseClass);
//        attendance.setCreatedBy(createBy);
//
//        return attendanceRepository.save(attendance);
//    }

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

        attendanceRepository.save(attendance);

        return "Check-in successful!";
    }
}
