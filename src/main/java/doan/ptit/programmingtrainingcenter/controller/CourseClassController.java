package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.request.CourseClassRequest;
import doan.ptit.programmingtrainingcenter.dto.response.PagedResponse;
import doan.ptit.programmingtrainingcenter.entity.CourseClass;
import doan.ptit.programmingtrainingcenter.entity.User;
import doan.ptit.programmingtrainingcenter.security.CustomUserDetails;
import doan.ptit.programmingtrainingcenter.service.CourseClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/student/select")
    public List<CourseClass> getClassCourse(@RequestParam(required = false, defaultValue = "") String courseId) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        return courseClassService.getClassByCourse(courseId,currentUser.getId());
    }
    @GetMapping("/all")
    public ResponseEntity<PagedResponse<CourseClass>> getClasses(
            @RequestParam(value = "className", required = false) String className,
            @RequestParam(value = "courseId", required = false) String courseId,
            @RequestParam(value = "instructorId", required = false) String instructorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<CourseClass> classes = courseClassService.getClassesWithFilters(className, courseId, instructorId, pageable);
        PagedResponse<CourseClass> response = new PagedResponse<>(classes);
        return ResponseEntity.ok(response);
    }


}
