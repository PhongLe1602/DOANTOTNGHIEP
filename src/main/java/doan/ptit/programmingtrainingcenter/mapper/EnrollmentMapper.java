package doan.ptit.programmingtrainingcenter.mapper;

import doan.ptit.programmingtrainingcenter.dto.request.EnrollmentRequest;
import doan.ptit.programmingtrainingcenter.entity.Enrollment;
import doan.ptit.programmingtrainingcenter.entity.Course;
import doan.ptit.programmingtrainingcenter.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enrollmentDate", ignore = true)
    @Mapping(target = "lastAccessed", ignore = true)
    @Mapping(target = "progress", constant = "0.00")
    @Mapping(target = "status", constant = "PENDING")
    Enrollment toEntity(EnrollmentRequest enrollmentRequest, User user, Course course);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enrollmentDate", ignore = true)
    @Mapping(target = "lastAccessed", ignore = true)
    void updateEnrollment(@MappingTarget Enrollment enrollment, EnrollmentRequest enrollmentRequest);
}
