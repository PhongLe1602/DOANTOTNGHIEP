package doan.ptit.programmingtrainingcenter.dto.response;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseClassResponse {
    String id;
    String name;
    String courseName;
    String instructorName;
    Date startDate;
    Date endDate;
    int totalSessions;

}
