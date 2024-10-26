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

    @Mapping(target = "id", ignore = true) // Bỏ qua ID khi tạo mới
    @Mapping(target = "enrollmentDate", ignore = true) // Bỏ qua thời gian tạo
    @Mapping(target = "lastAccessed", ignore = true) // Bỏ qua thời gian cập nhật
    @Mapping(target = "progress", constant = "0.00") // Tiến độ mặc định là 0%
    @Mapping(target = "status", constant = "PENDING") // Trạng thái mặc định là PENDING
    Enrollment toEntity(EnrollmentRequest enrollmentRequest, User user, Course course);

    @Mapping(target = "id", ignore = true) // Bỏ qua ID khi cập nhật
    @Mapping(target = "enrollmentDate", ignore = true) // Bỏ qua thời gian tạo
    @Mapping(target = "lastAccessed", ignore = true) // Bỏ qua thời gian cập nhật
    void updateEnrollment(@MappingTarget Enrollment enrollment, EnrollmentRequest enrollmentRequest);
}
