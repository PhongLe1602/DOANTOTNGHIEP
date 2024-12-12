package doan.ptit.programmingtrainingcenter.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceSessionDetailResponse {
    private String sessionId;
    private String className;
    private String classId;
    private Date startTime;
    private long duration;
    private List<StudentAttendanceResponse> attendedStudents;
    private List<StudentAttendanceResponse> notAttendedStudents;
}
