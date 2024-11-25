package doan.ptit.programmingtrainingcenter.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ScheduleRequest {

    @NotNull(message = "CourseClass ID is required")
    String courseClassId;

    @NotNull(message = "Session type is required")
    SessionType sessionType;

    @Future(message = "Session date must be in the future")
    @NotNull(message = "Session date is required")
    Date sessionDate;

    @NotNull(message = "Start time is required")
    Date startTime;

    @NotNull(message = "End time is required")
    Date endTime;

    @NotNull(message = "Duration is required")
    Integer duration;

    String description;

    String instructorId;

    // Dành riêng cho buổi học offline
    String location; // Địa điểm cho buổi học offline

    // Dành riêng cho buổi học online
    String onlineLink; // Link Zoom cho buổi học online

    public enum SessionType {
        ONLINE,
        OFFLINE,
        VIDEO // Nếu cần thêm loại video
    }
}
