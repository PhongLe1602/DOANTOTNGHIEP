package doan.ptit.programmingtrainingcenter.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseClassScheduleResponse {
    CourseClassResponse courseClass;
    List<ScheduleListResponse> scheduleList;
}
