package doan.ptit.programmingtrainingcenter.service;


import doan.ptit.programmingtrainingcenter.dto.request.AttendanceSessionRequest;
import doan.ptit.programmingtrainingcenter.dto.response.AttendanceSessionDetailResponse;
import doan.ptit.programmingtrainingcenter.entity.AttendanceSession;
import doan.ptit.programmingtrainingcenter.entity.CourseClass;
import doan.ptit.programmingtrainingcenter.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AttendanceSessionService {
    AttendanceSession createSession(AttendanceSessionRequest attendanceSessionRequest , String instructorId);
    AttendanceSessionDetailResponse getAttendanceSessionDetail(String sessionId);
    List<AttendanceSession> getAttendanceSessionsOfClass(String classId);
}
