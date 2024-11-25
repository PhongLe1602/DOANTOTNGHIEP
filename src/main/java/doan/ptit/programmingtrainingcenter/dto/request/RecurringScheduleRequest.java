package doan.ptit.programmingtrainingcenter.dto.request;

import doan.ptit.programmingtrainingcenter.entity.Schedule;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class RecurringScheduleRequest {

    private String courseClassId;
    private String instructorId;
    private String description;
    private int duration; // thời lượng buổi học (phút)
    private LocalDate startDate; // ngày bắt đầu
    private LocalDate endDate; // ngày kết thúc
    private LocalTime startTime; // giờ bắt đầu buổi học
    private List<Integer> daysOfWeek; // các ngày trong tuần để lặp lại
    private Schedule.SessionType sessionType; // loại buổi học: ONLINE, OFFLINE, VIDEO
    private String onlineLink; // link học online
    private String location; // địa điểm học (nếu OFFLINE)
}
