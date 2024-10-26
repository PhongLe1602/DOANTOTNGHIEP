package doan.ptit.programmingtrainingcenter.service.impl;

import doan.ptit.programmingtrainingcenter.dto.request.CoursesRequest;
import doan.ptit.programmingtrainingcenter.dto.response.CoursesResponse;
import doan.ptit.programmingtrainingcenter.entity.Course;
import doan.ptit.programmingtrainingcenter.entity.Section;
import doan.ptit.programmingtrainingcenter.repository.CourseRepository;
import doan.ptit.programmingtrainingcenter.repository.EnrollmentRepository;
import doan.ptit.programmingtrainingcenter.repository.SectionRepository;
import doan.ptit.programmingtrainingcenter.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SectionRepository sectionRepository;


    @Override
    public CoursesResponse getCourseById(String id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Courses Not Found"));
        List<Section>  sectionList =  sectionRepository.findByCourseId(id);
        return CoursesResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .category(course.getCategory())
                .level(course.getLevel())
                .description(course.getDescription())
                .price(course.getPrice())
                .studentCount(course.getStudentCount())
                .duration(course.getDuration())
                .thumbnail(course.getThumbnail())
                .sectionList(sectionList)
                .instructorList(course.getInstructors())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();
    }

    @Override
    public List<Course> getCourse() {
        return courseRepository.findAll();
    }

    @Override
    public Course addCourse(CoursesRequest coursesRequest) {
        Course course = new Course();
        course.setTitle(coursesRequest.getTitle());
        course.setDuration(coursesRequest.getDuration());
        course.setDescription(coursesRequest.getDescription());
        course.setPrice(BigDecimal.valueOf(coursesRequest.getPrice()));
        course.setLevel(Course.Level.valueOf(coursesRequest.getLevel()));
        course.setTitle(coursesRequest.getTitle());
        course.setCreatedAt(new Date());
        course.setUpdatedAt(new Date());
        return courseRepository.save(course);
    }


}
