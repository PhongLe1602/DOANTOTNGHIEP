package doan.ptit.programmingtrainingcenter.dto.response;


import doan.ptit.programmingtrainingcenter.entity.Course;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CoursesListResponse {

    String id;

    String title;

    int duration;

    Course.Level level;

    BigDecimal price;

    String thumbnail;

    int studentCount;

    String description;

    CategoryResponse  category;

    List<InstructorResponse> instructors;


}
