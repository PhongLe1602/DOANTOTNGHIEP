package doan.ptit.programmingtrainingcenter.dto.response;


import doan.ptit.programmingtrainingcenter.entity.Assignment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class AssignmentResponse {
    String id;
    CourseClassResponse courseClass;
    String title;
    String description;
    Assignment.Type type;
    String fileUrl;
    Date dueDate;

}
