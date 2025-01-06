package doan.ptit.programmingtrainingcenter.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import doan.ptit.programmingtrainingcenter.dto.request.CoursesRequest;
import doan.ptit.programmingtrainingcenter.dto.response.*;
import doan.ptit.programmingtrainingcenter.entity.*;
import doan.ptit.programmingtrainingcenter.mapper.CategoryMapper;
import doan.ptit.programmingtrainingcenter.mapper.CourseMapper;
import doan.ptit.programmingtrainingcenter.mapper.SectionMapper;
import doan.ptit.programmingtrainingcenter.mapper.UserMapper;
import doan.ptit.programmingtrainingcenter.repository.*;
import doan.ptit.programmingtrainingcenter.service.CourseService;
import doan.ptit.programmingtrainingcenter.specification.SearchCriteria;
import doan.ptit.programmingtrainingcenter.specification.SpecificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    private CourseMapper courseMapper;

    @Autowired
    private UserMapper userMapper;

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
        CoursesResponse coursesResponse = courseMapper.toCoursesResponse(course);
        coursesResponse.setSectionList(sectionResponseList);
        return coursesResponse;
    }

    @Override
    public Page<CoursesListResponse> getCourses(int page, int size, String sortBy, String sortDirection , List<SearchCriteria> filters) {
        Sort sort = sortDirection.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        SpecificationBuilder<Course> builder = new SpecificationBuilder<>();

        for (SearchCriteria criteria : filters) {
            builder.with(criteria.getKey(), criteria.getOperation(), criteria.getValue());
        }

        Specification<Course> specification = builder.build();

        Page<Course> coursesPage = courseRepository.findAll(specification, pageable);

        return coursesPage.map(course -> courseMapper.toCourseListResponse(course));
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
                String folderName = "programming-training-center-project/courses";
                Map<?, ?> uploadResult = cloudinary.uploader().upload(
                        coursesRequest.getThumbnail().getBytes(),
                        ObjectUtils.asMap(
                                "folder", folderName // Thư mục đích trên Cloudinary
                        )
                );
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

        Category category = courseCategoryRepository.findById(coursesRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category Not Found"));
        // Update fields from request
        existingCourse.setTitle(coursesRequest.getTitle());
        existingCourse.setDescription(coursesRequest.getDescription());
        existingCourse.setDuration(coursesRequest.getDuration());
        existingCourse.setPrice(BigDecimal.valueOf(coursesRequest.getPrice()));
        existingCourse.setLevel(Course.Level.valueOf(coursesRequest.getLevel()));
        existingCourse.setCategory(category);
        existingCourse.setUpdatedAt(new Date());

        // Upload new thumbnail if provided
        if (coursesRequest.getThumbnail() != null && !coursesRequest.getThumbnail().isEmpty()) {
            try {
                String folderName = "programming-training-center-project/courses";
                Map<?, ?> uploadResult = cloudinary.uploader().upload(
                        coursesRequest.getThumbnail().getBytes(),
                        ObjectUtils.asMap(
                                "folder", folderName // Thư mục đích trên Cloudinary
                        )
                );
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

    @Override
    public List<Course> getCourseByInstructor(String instructorId) {
        return courseRepository.findCoursesByInstructorId(instructorId);
    }

    @Override
    public List<Course> searchCourses(String query) {
        return courseRepository.findByTitleContainingOrDescriptionContaining(query, query);
    }

    @Override
    public List<CoursesListResponse> getAllCourses() {
        List<Course> listCourses = courseRepository.findAll();
        return listCourses.stream()
                .map(courseMapper::toCourseListResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<InstructorResponse> getInstructorsByCourseId(String courseId) {
        List<User> instrucrtorsList =  courseRepository.findInstructorsByCourseId(courseId);
        return userMapper.toListInstructorResponse(instrucrtorsList);

    }

    @Override
    public List<CoursesListResponse> getCoursesByCategoryType(List<Category.CategoryType> types) {
        List<Course> filteredCourses = courseRepository.findByCategoryTypeIn(types);
        return filteredCourses.stream()
                .map(courseMapper::toCourseListResponse)
                .collect(Collectors.toList());
    }



}
