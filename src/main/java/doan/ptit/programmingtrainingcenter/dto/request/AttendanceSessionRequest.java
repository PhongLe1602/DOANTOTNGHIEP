package doan.ptit.programmingtrainingcenter.dto.request;


import lombok.Data;

@Data
public class AttendanceSessionRequest {
    private String classId;
    private int durationMinutes;
}
