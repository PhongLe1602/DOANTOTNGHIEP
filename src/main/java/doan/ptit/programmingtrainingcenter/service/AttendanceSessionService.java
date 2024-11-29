package doan.ptit.programmingtrainingcenter.service;


import doan.ptit.programmingtrainingcenter.dto.request.AttendanceSessionRequest;
import doan.ptit.programmingtrainingcenter.entity.AttendanceSession;
import doan.ptit.programmingtrainingcenter.entity.CourseClass;
import doan.ptit.programmingtrainingcenter.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface AttendanceSessionService {
    AttendanceSession createSession(AttendanceSessionRequest attendanceSessionRequest , String instructorId);
}
