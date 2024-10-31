package doan.ptit.programmingtrainingcenter.dto.response;

import doan.ptit.programmingtrainingcenter.entity.Course;
import doan.ptit.programmingtrainingcenter.entity.Schedule.SessionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponse {

    private String id;
    private Course course;
    private SessionType sessionType;
    private Date sessionDate;
    private Date startTime;
    private Date endTime;
    private Integer duration;
    private String description;
    private String instructorName;
    private String onlineLink;
    private String location;
    private Date createdAt;
    private Date updatedAt;
}
