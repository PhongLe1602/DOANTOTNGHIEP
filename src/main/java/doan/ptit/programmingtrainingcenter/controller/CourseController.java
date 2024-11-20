package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.request.CoursesRequest;
import doan.ptit.programmingtrainingcenter.dto.response.CoursesResponse;
import doan.ptit.programmingtrainingcenter.entity.Course;
import doan.ptit.programmingtrainingcenter.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;


    @GetMapping
    List<Course> getCourseList(){
        return courseService.getCourse();
    }
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    Course createCourse(@ModelAttribute CoursesRequest coursesRequest){
        return courseService.addCourse(coursesRequest);
    }

    @GetMapping("/{id}")
    CoursesResponse getCourseById(@PathVariable String id){
        return courseService.getCourseById(id);
    }

    @GetMapping("/user/{userId}/enrollments")
    public List<Course> getCoursesByUser(@PathVariable String userId) {
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
}
