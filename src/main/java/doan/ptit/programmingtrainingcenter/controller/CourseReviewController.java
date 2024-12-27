package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.request.CourseReviewRequest;
import doan.ptit.programmingtrainingcenter.dto.response.PagedResponse;
import doan.ptit.programmingtrainingcenter.entity.CourseReview;
import doan.ptit.programmingtrainingcenter.security.CustomUserDetails;
import doan.ptit.programmingtrainingcenter.service.CourseReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
public class CourseReviewController {

    @Autowired
    private CourseReviewService courseReviewService;

    @GetMapping("/users/{userId}")
    public List<CourseReview> getReviewOfUser(@PathVariable("userId") String userId) {
        return courseReviewService.getReviewsByUserId(userId);
    }
    @GetMapping("/courses/{courseId}")
    public List<CourseReview> getReviewOfCourse(@PathVariable("courseId") String courseId) {
        return courseReviewService.getReviewsByCourseId(courseId);
    }
    @PostMapping
    public CourseReview addReview(@RequestBody CourseReviewRequest courseReviewRequest) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        return courseReviewService.createReview(currentUser.getId(),courseReviewRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable("id") String id) {
        courseReviewService.deleteReview(id);
    }

    @GetMapping
    public PagedResponse<CourseReview> getReviewsWithFilterAndPagination(
            @RequestParam(value = "courseId", required = false) String courseId,
            @RequestParam(value = "rating", required = false) Integer rating,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<CourseReview> courseReviewPage = courseReviewService.getReviewsWithFilterAndPagination(courseId, rating, keyword, page, size);
        return new PagedResponse<>(courseReviewPage);
    }





}
