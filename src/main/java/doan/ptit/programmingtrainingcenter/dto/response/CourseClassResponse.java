package doan.ptit.programmingtrainingcenter.dto.response;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseClassResponse {
    String id;
    String name;
    String courseName;
    String instructorName;

}
