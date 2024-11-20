package doan.ptit.programmingtrainingcenter.service.impl;

import doan.ptit.programmingtrainingcenter.dto.request.CourseReviewRequest;
import doan.ptit.programmingtrainingcenter.entity.Course;
import doan.ptit.programmingtrainingcenter.entity.CourseReview;
import doan.ptit.programmingtrainingcenter.entity.User;
import doan.ptit.programmingtrainingcenter.repository.CourseRepository;
import doan.ptit.programmingtrainingcenter.repository.CourseReviewRepository;
import doan.ptit.programmingtrainingcenter.repository.UserRepository;
import doan.ptit.programmingtrainingcenter.service.CourseReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CourseReviewServiceImpl implements CourseReviewService {

    @Autowired
    private CourseReviewRepository courseReviewRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public CourseReview getReviewById(String id) {
        return courseReviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Courses Not Found"));
    }

    @Override
    public List<CourseReview> getReviewsByCourseId(String courseId) {
        return courseReviewRepository.findByCourseId(courseId);
    }

    @Override
    public List<CourseReview> getReviewsByUserId(String userId) {
        return courseReviewRepository.findByUserId(userId);
    }

    @Override
    public CourseReview createReview(CourseReviewRequest courseReviewRequest) {
        Course course = courseRepository.findById(courseReviewRequest.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course Not Found"));
        User user = userRepository.findById(courseReviewRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        CourseReview courseReview = new CourseReview();
        courseReview.setCourse(course);
        courseReview.setUser(user);
        courseReview.setRating(courseReviewRequest.getRating());
        courseReview.setReview(courseReviewRequest.getReview());


        return courseReviewRepository.save(courseReview);
    }

    @Override
    public void deleteReview(String id) {
        courseReviewRepository.deleteById(id);
    }
}
