package doan.ptit.programmingtrainingcenter.dto.response;


import doan.ptit.programmingtrainingcenter.entity.Category;
import doan.ptit.programmingtrainingcenter.entity.Course;
import doan.ptit.programmingtrainingcenter.entity.Section;
import doan.ptit.programmingtrainingcenter.entity.User;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class CoursesResponse {

    String id;


    String title;


    String description;


    int duration;


    Course.Level level;


    BigDecimal price;


    String thumbnail;


    int studentCount;


    Category category;

    List<Section> sectionList;

    List<User> instructorList;

    Date createdAt;


    Date updatedAt;

    public enum Level {
        BEGINNER,
        INTERMEDIATE,
        ADVANCED
    }
}
