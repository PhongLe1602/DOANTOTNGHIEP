package doan.ptit.programmingtrainingcenter.mapper;


import doan.ptit.programmingtrainingcenter.dto.request.CourseClassRequest;
import doan.ptit.programmingtrainingcenter.dto.response.CourseClassResponse;
import doan.ptit.programmingtrainingcenter.entity.CourseClass;
import doan.ptit.programmingtrainingcenter.entity.Course;
import doan.ptit.programmingtrainingcenter.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CourseClassMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "course", source = "course")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "instructor", source = "instructor")
    CourseClass toClass(CourseClassRequest courseClassRequest , Course course, User instructor);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
//    @Mapping(target = "status", ignore = true)
    @Mapping(target = "course", source = "course")
    @Mapping(target = "instructor", source = "instructor")
    void updateClass(@MappingTarget CourseClass courseClassUpdate, CourseClassRequest courseClassRequest , Course course , User instructor);


    @Mapping(target = "instructorName", source = "instructor.fullName")
    @Mapping(target = "courseName", source = "course.title")
    CourseClassResponse toResponse(CourseClass courseClass);
}
