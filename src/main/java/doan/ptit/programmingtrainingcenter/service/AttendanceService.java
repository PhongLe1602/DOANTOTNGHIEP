package doan.ptit.programmingtrainingcenter.service;


import doan.ptit.programmingtrainingcenter.dto.request.AttendanceRequest;
import doan.ptit.programmingtrainingcenter.dto.response.AttendanceSessionDetailResponse;
import doan.ptit.programmingtrainingcenter.entity.Attendance;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AttendanceService {
    List<Attendance> getAllAttendance();
    Attendance getAttendanceById(String id);
//    Attendance addAttendance(AttendanceRequest attendanceRequest);
//    Attendance updateAttendance(String id ,AttendanceRequest attendanceRequest);
    void deleteAttendance(String id);
    String checkIn(String sessionId,String studentId);
    List<Attendance> getAttendanceBySessionId(String sessionId);
    List<Attendance> getAttendanceByStudentId(String studentId);

}
