package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.request.CoursesRequest;
import doan.ptit.programmingtrainingcenter.dto.response.ApiResponse;
import doan.ptit.programmingtrainingcenter.dto.response.CoursesListResponse;
import doan.ptit.programmingtrainingcenter.dto.response.CoursesResponse;
import doan.ptit.programmingtrainingcenter.dto.response.PagedResponse;
import doan.ptit.programmingtrainingcenter.entity.Course;
import doan.ptit.programmingtrainingcenter.security.CustomUserDetails;
import doan.ptit.programmingtrainingcenter.service.CourseService;
import doan.ptit.programmingtrainingcenter.specification.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;


    @GetMapping
    public ApiResponse<PagedResponse<CoursesListResponse>>  getCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(required = false) String key,
            @RequestParam(required = false) String operation,
            @RequestParam(required = false) String value


    ) {
        List<SearchCriteria> filters = new ArrayList<>();
        if (key != null && operation != null && value != null) {
            filters.add(new SearchCriteria(key, operation, value));
        }
        Page<CoursesListResponse> coursePage = courseService.getCourses(page, size, sortBy,sortDirection, filters);
        PagedResponse<CoursesListResponse> response = new PagedResponse<>(coursePage);
        return ApiResponse.success("Courses retrieved successfully", response);
    }
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    Course createCourse(@ModelAttribute CoursesRequest coursesRequest){
        return courseService.addCourse(coursesRequest);
    }

    @GetMapping("/{id}")
    ApiResponse<CoursesResponse> getCourseById(@PathVariable String id){
        CoursesResponse response = courseService.getCourseById(id);
        return ApiResponse.success("Course retrieved successfully", response);
    }

    @GetMapping("/user/{userId}/enrollments")
    public List<Course> getCoursesByUserId(@PathVariable String userId) {
        return courseService.getCoursesByUser(userId);
    }
    @PutMapping("/{id}")
    public Course updateCourse(@PathVariable String id, @ModelAttribute CoursesRequest request) {
        return courseService.updateCourse(id, request);
    }
    @DeleteMapping("/{id}")
    public boolean deleteCourse(@PathVariable String id) {
        return courseService.deleteCourse(id);
    }
    @GetMapping("/user")
    public List<Course> getCoursesByUser() {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return courseService.getCoursesByUser(currentUser.getId());
    }
    @GetMapping("/instructor")
    public List<Course> getCoursesByInstructor() {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return courseService.getCourseByInstructor(currentUser.getId());
    }
    @GetMapping("/search")
    public List<Course> searchCourses(@RequestParam String q) {
        return courseService.searchCourses(q);
    }

    @GetMapping("/all")
    public List<CoursesListResponse> getAll() {
        return courseService.getAllCourses();
    }

}
