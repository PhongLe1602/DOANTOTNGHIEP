package doan.ptit.programmingtrainingcenter.service.impl;

import doan.ptit.programmingtrainingcenter.dto.request.CoursesRequest;
import doan.ptit.programmingtrainingcenter.dto.response.CategoryResponse;
import doan.ptit.programmingtrainingcenter.dto.response.CoursesResponse;
import doan.ptit.programmingtrainingcenter.dto.response.SectionResponse;
import doan.ptit.programmingtrainingcenter.entity.Course;
import doan.ptit.programmingtrainingcenter.entity.Enrollment;
import doan.ptit.programmingtrainingcenter.entity.Section;
import doan.ptit.programmingtrainingcenter.mapper.CategoryMapper;
import doan.ptit.programmingtrainingcenter.mapper.SectionMapper;
import doan.ptit.programmingtrainingcenter.repository.CourseRepository;
import doan.ptit.programmingtrainingcenter.repository.EnrollmentRepository;
import doan.ptit.programmingtrainingcenter.repository.SectionRepository;
import doan.ptit.programmingtrainingcenter.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SectionMapper sectionMapper;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Override
    public CoursesResponse getCourseById(String id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Courses Not Found"));
        List<Section>  sectionList =  sectionRepository.findByCourseId(id);
        CategoryResponse categoryResponse = categoryMapper.toCategoryResponse(course.getCategory());
        List<SectionResponse> sectionResponseList = sectionMapper.toDtoList(sectionList);
        return CoursesResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .category(categoryResponse)
                .level(course.getLevel())
                .description(course.getDescription())
                .price(course.getPrice())
                .studentCount(course.getStudentCount())
                .duration(course.getDuration())
                .thumbnail(course.getThumbnail())
                .sectionList(sectionResponseList)
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

    @Override
    public List<Course> getCoursesByUser(String userId) {
        List<Enrollment> enrollments = enrollmentRepository.findByUserId(userId);
        return enrollments.stream()
                .map(Enrollment::getCourse)
                .collect(Collectors.toList());
    }

    @Override
    public Course updateCourse(String courseId, CoursesRequest coursesRequest) {
        Course existingCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học với ID: " + courseId));

        // Cập nhật thông tin từ request
        existingCourse.setTitle(coursesRequest.getTitle());
        existingCourse.setDescription(coursesRequest.getDescription());
        existingCourse.setDuration(coursesRequest.getDuration());
        existingCourse.setPrice(BigDecimal.valueOf(coursesRequest.getPrice()));
        existingCourse.setLevel(Course.Level.valueOf(coursesRequest.getLevel()));
        existingCourse.setThumbnail(coursesRequest.getThumbnail());
        existingCourse.setUpdatedAt(new Date());

        // Lưu vào database
        return courseRepository.save(existingCourse);
    }

    @Override
    public Boolean deleteCourse(String courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Courses Not Found"));
        courseRepository.delete(course);

        return true;
    }


}
