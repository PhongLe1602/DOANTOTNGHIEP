package doan.ptit.programmingtrainingcenter.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import doan.ptit.programmingtrainingcenter.dto.request.CoursesRequest;
import doan.ptit.programmingtrainingcenter.dto.response.CategoryResponse;
import doan.ptit.programmingtrainingcenter.dto.response.CoursesResponse;
import doan.ptit.programmingtrainingcenter.dto.response.SectionResponse;
import doan.ptit.programmingtrainingcenter.entity.*;
import doan.ptit.programmingtrainingcenter.mapper.CategoryMapper;
import doan.ptit.programmingtrainingcenter.mapper.SectionMapper;
import doan.ptit.programmingtrainingcenter.repository.*;
import doan.ptit.programmingtrainingcenter.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private CategoryRepository courseCategoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public CoursesResponse getCourseById(String id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Courses Not Found"));
        List<Section> sectionList = sectionRepository.findByCourseId(id);
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
    @Transactional
    public Course addCourse(CoursesRequest coursesRequest) {
        // Tìm category
        Category category = courseCategoryRepository.findById(coursesRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category Not Found"));

        // Tìm danh sách giảng viên dựa trên instructorIds
        List<User> instructors = userRepository.findAllById(coursesRequest.getInstructorIds());

        // Khởi tạo Course
        Course course = Course.builder()
                .title(coursesRequest.getTitle())
                .duration(coursesRequest.getDuration())
                .description(coursesRequest.getDescription())
                .price(BigDecimal.valueOf(coursesRequest.getPrice()))
                .level(Course.Level.valueOf(coursesRequest.getLevel()))
                .category(category)
                .instructors(instructors) // Gắn danh sách giảng viên
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        // Upload thumbnail
        if (coursesRequest.getThumbnail() != null && !coursesRequest.getThumbnail().isEmpty()) {
            try {
                Map<?, ?> uploadResult = cloudinary.uploader().upload(coursesRequest.getThumbnail().getBytes(), ObjectUtils.emptyMap());
                course.setThumbnail((String) uploadResult.get("url"));
            } catch (Exception e) {
                throw new RuntimeException("Error uploading image to Cloudinary", e);
            }
        }

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

        // Update fields from request
        existingCourse.setTitle(coursesRequest.getTitle());
        existingCourse.setDescription(coursesRequest.getDescription());
        existingCourse.setDuration(coursesRequest.getDuration());
        existingCourse.setPrice(BigDecimal.valueOf(coursesRequest.getPrice()));
        existingCourse.setLevel(Course.Level.valueOf(coursesRequest.getLevel()));
        existingCourse.setUpdatedAt(new Date());

        // Upload new thumbnail if provided
        if (coursesRequest.getThumbnail() != null && !coursesRequest.getThumbnail().isEmpty()) {
            try {
                Map<?, ?> uploadResult = cloudinary.uploader().upload(coursesRequest.getThumbnail().getBytes(), ObjectUtils.emptyMap());
                existingCourse.setThumbnail((String) uploadResult.get("url")); // Update thumbnail URL
            } catch (Exception e) {
                throw new RuntimeException("Error uploading image to Cloudinary", e);
            }
        }

        if (coursesRequest.getInstructorIds() != null && !coursesRequest.getInstructorIds().isEmpty()) {
            List<User> instructors = userRepository.findAllById(coursesRequest.getInstructorIds());
            existingCourse.setInstructors(instructors); // Gán giảng viên cho khóa học
        }
        return courseRepository.save(existingCourse);
    }

    @Override
    public Boolean deleteCourse(String courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Courses Not Found"));
        courseRepository.delete(course);
        return true;
    }
}
