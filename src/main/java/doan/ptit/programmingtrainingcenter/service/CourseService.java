package doan.ptit.programmingtrainingcenter.service;

import doan.ptit.programmingtrainingcenter.dto.request.CoursesRequest;
import doan.ptit.programmingtrainingcenter.dto.response.CoursesListResponse;
import doan.ptit.programmingtrainingcenter.dto.response.CoursesResponse;
import doan.ptit.programmingtrainingcenter.entity.Course;
import doan.ptit.programmingtrainingcenter.specification.SearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface CourseService {
    CoursesResponse getCourseById(String id);
    Page<CoursesListResponse> getCourses(int page, int size, String sortBy, String sortDirection, List<SearchCriteria> searchCriteriaList);
    Course addCourse(CoursesRequest coursesRequest);
    List<Course> getCoursesByUser(String userId);
    Course updateCourse(String courseId, CoursesRequest coursesRequest);
    Boolean deleteCourse(String courseId);
    List<Course> getCourseByInstructor(String instructorId);
    List<Course> searchCourses(String query);

}
