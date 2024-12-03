package doan.ptit.programmingtrainingcenter.service;


import doan.ptit.programmingtrainingcenter.dto.request.CourseReviewRequest;
import doan.ptit.programmingtrainingcenter.entity.CourseReview;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CourseReviewService {
    CourseReview getReviewById (String id);
    List<CourseReview> getReviewsByCourseId (String courseId);
    List<CourseReview> getReviewsByUserId (String userId);
    CourseReview createReview (String userId,CourseReviewRequest courseReviewRequest);
    void deleteReview (String id);
}
