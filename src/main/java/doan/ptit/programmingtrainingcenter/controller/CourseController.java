package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.request.CoursesRequest;
import doan.ptit.programmingtrainingcenter.dto.response.CoursesResponse;
import doan.ptit.programmingtrainingcenter.entity.Course;
import doan.ptit.programmingtrainingcenter.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @PostMapping
    Course createCourse(@RequestBody CoursesRequest coursesRequest){
        return courseService.addCourse(coursesRequest);
    }

    @GetMapping("/{id}")
    CoursesResponse getCourseById(@PathVariable String id){
        return courseService.getCourseById(id);
    }
}
