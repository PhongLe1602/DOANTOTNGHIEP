package doan.ptit.programmingtrainingcenter.dto.request;


import lombok.Data;

@Data
public class LessonRequest {
    private String sectionId;
    private String title;
    private String content;
    private int duration;
    private String videoLink;

}
