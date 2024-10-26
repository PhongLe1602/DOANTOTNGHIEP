package doan.ptit.programmingtrainingcenter.service;

import doan.ptit.programmingtrainingcenter.dto.request.CoursesRequest;
import doan.ptit.programmingtrainingcenter.dto.response.CoursesResponse;
import doan.ptit.programmingtrainingcenter.entity.Course;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface CourseService {
    CoursesResponse getCourseById(String id);
    List<Course> getCourse();
    Course addCourse(CoursesRequest coursesRequest);

}
