package doan.ptit.programmingtrainingcenter.mapper;


import doan.ptit.programmingtrainingcenter.dto.request.CourseClassRequest;
import doan.ptit.programmingtrainingcenter.entity.CourseClass;
import doan.ptit.programmingtrainingcenter.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CourseClassMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "course", source = "course")
    @Mapping(target = "status", constant = "ACTIVE")
    CourseClass toClass(CourseClassRequest courseClassRequest , Course course);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "course", source = "course")
    void updateClass(@MappingTarget CourseClass courseClassUpdate, CourseClassRequest courseClassRequest , Course course);


}
