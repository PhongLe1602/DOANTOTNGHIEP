package doan.ptit.programmingtrainingcenter.dto.response;


import doan.ptit.programmingtrainingcenter.entity.CourseClass;
import doan.ptit.programmingtrainingcenter.entity.Schedule;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduleListResponse {

     String id;
     Schedule.SessionType sessionType;
     Date sessionDate;
     Date startTime;
     Date endTime;
     Integer duration;
     String description;
//     String instructorName;
     String onlineLink;
     String location;
     Date createdAt;
     Date updatedAt;

}
