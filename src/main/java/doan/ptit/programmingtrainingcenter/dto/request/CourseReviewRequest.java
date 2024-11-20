package doan.ptit.programmingtrainingcenter.dto.request;


import lombok.Data;

@Data
public class CourseReviewRequest {
    private String courseId;
    private String userId;
    private String review;
    private int rating;
}
