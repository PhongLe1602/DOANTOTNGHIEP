package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.request.CourseClassRequest;
import doan.ptit.programmingtrainingcenter.entity.CourseClass;
import doan.ptit.programmingtrainingcenter.entity.User;
import doan.ptit.programmingtrainingcenter.security.CustomUserDetails;
import doan.ptit.programmingtrainingcenter.service.CourseClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
public class CourseClassController {

    @Autowired
    private CourseClassService courseClassService;

    @GetMapping
    public List<CourseClass> getClasses() {
        return courseClassService.getAllClasses();
    }

    @GetMapping("/{id}")
    public CourseClass getClass(@PathVariable String id) {
        return courseClassService.getClassById(id);
    }

    @PostMapping
    public CourseClass addClass(@RequestBody CourseClassRequest courseClassRequest) {
        return courseClassService.createClass(courseClassRequest);
    }
    @PutMapping("/{id}")
    public CourseClass updateClass(@PathVariable String id, @RequestBody CourseClassRequest courseClassRequest) {
        return courseClassService.updateClass(id, courseClassRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteClass(@PathVariable String id) {
        courseClassService.deleteClass(id);
    }
    @GetMapping("/course/{courseId}")
    public List<CourseClass> getClassByCourseId(@PathVariable String courseId) {
        return courseClassService.getClassByCourseId(courseId);
    }

    @GetMapping("{classId}/students")
    public List<User> getStudentOfClass(@PathVariable String classId) {
        return courseClassService.getStudentsByClassId(classId);
    }
    @GetMapping("/instructor")
    public List<CourseClass> getClassInstructor() {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        return courseClassService.getClassByInstructorId(currentUser.getId());
    }
    @GetMapping("/student")
    public List<CourseClass> getClassStudent() {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        return courseClassService.getClassesByStudentId(currentUser.getId());
    }

}
