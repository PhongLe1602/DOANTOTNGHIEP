package doan.ptit.programmingtrainingcenter.mapper;

import doan.ptit.programmingtrainingcenter.dto.response.CategoryResponse;
import doan.ptit.programmingtrainingcenter.dto.response.CoursesListResponse;
import doan.ptit.programmingtrainingcenter.dto.response.CoursesResponse;
import doan.ptit.programmingtrainingcenter.dto.response.InstructorResponse;
import doan.ptit.programmingtrainingcenter.entity.Category;
import doan.ptit.programmingtrainingcenter.entity.Course;
import doan.ptit.programmingtrainingcenter.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    @Mapping(source = "category", target = "category")
    @Mapping(source = "instructors", target = "instructorList")
    CoursesResponse toCoursesResponse(Course course);

    CategoryResponse toCategoryResponse(Category category);


    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "profilePicture", target = "profilePicture")
    InstructorResponse toInstructorResponse(User user);

    List<InstructorResponse> toInstructorResponseList(List<User> users);

    @Mapping(source = "category", target = "category")
    @Mapping(source = "instructors", target = "instructors")
    CoursesListResponse toCourseListResponse(Course course);

}
