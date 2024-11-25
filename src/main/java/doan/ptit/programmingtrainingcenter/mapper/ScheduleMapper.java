package doan.ptit.programmingtrainingcenter.mapper;

import doan.ptit.programmingtrainingcenter.dto.request.ScheduleRequest;
import doan.ptit.programmingtrainingcenter.dto.response.ScheduleResponse;
import doan.ptit.programmingtrainingcenter.entity.Course;
import doan.ptit.programmingtrainingcenter.entity.CourseClass;
import doan.ptit.programmingtrainingcenter.entity.Schedule;
import doan.ptit.programmingtrainingcenter.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "courseClass", source = "courseClass")
    @Mapping(target = "instructor", source = "instructor")
    @Mapping(target = "duration", source = "scheduleRequest.duration")
    @Mapping(target = "description", source = "scheduleRequest.description")
    @Mapping(target = "createdAt",ignore = true)
    @Mapping(target = "updatedAt",ignore = true)
    Schedule toEntity(ScheduleRequest scheduleRequest , CourseClass courseClass , User instructor);


    @Mapping(target = "courseClass", source = "courseClass") // Chuyển đổi từ course.id
    @Mapping(target = "instructorName", source = "instructor.fullName") // Chuyển đổi từ instructor.id
    ScheduleResponse toResponse(Schedule schedule);

    List<ScheduleResponse> toResponseList(List<Schedule> schedules);

}
