package doan.ptit.programmingtrainingcenter.dto.response;


import lombok.Data;

import java.util.Date;

@Data
public class LessonResponse {
    private String id;
    private String title;
    private String content;
    private String videoLink;
    private String duration;
    private Date createdAt;
}
