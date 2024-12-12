package doan.ptit.programmingtrainingcenter.dto.response;


import doan.ptit.programmingtrainingcenter.entity.Lesson;
import lombok.Data;

import java.util.List;

@Data
public class SectionResponse {
    private String id;
    private String title;
    private String description;
    List<LessonResponse> lessonList;

}
