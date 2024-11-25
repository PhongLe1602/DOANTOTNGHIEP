package doan.ptit.programmingtrainingcenter.dto.request;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseClassRequest {
    String name;
    String courseId;
    Date startDate;
    Date endDate;
    String studyTime;
    String studyDays;
    ClassStatus status;

    public enum ClassStatus {
        ACTIVE,
        COMPLETED,
        CANCELLED
    }

}
