package doan.ptit.programmingtrainingcenter.mapper;

import doan.ptit.programmingtrainingcenter.dto.request.ScheduleRequest;
import doan.ptit.programmingtrainingcenter.dto.response.CourseClassResponse;
import doan.ptit.programmingtrainingcenter.dto.response.CourseClassScheduleResponse;
import doan.ptit.programmingtrainingcenter.dto.response.ScheduleListResponse;
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
    @Mapping(target = "duration", source = "scheduleRequest.duration")
    @Mapping(target = "description", source = "scheduleRequest.description")
    @Mapping(target = "createdAt",ignore = true)
    @Mapping(target = "updatedAt",ignore = true)
    Schedule toEntity(ScheduleRequest scheduleRequest , CourseClass courseClass );


    @Mapping(target = "courseClass", source = "courseClass")
    ScheduleResponse toResponse(Schedule schedule);

    List<ScheduleResponse> toResponseList(List<Schedule> schedules);


//    @Mapping(target = "instructorName", source = "schedule.courseClass.instructor.fullName")
//    ScheduleListResponse toResponseScheduleList(Schedule schedule);
//
//    List<ScheduleListResponse> toResponseListSchedule(List<Schedule> schedules);

    @Mapping(target = "courseClass", source = "courseClassResponse")
    @Mapping(target = "scheduleList", source = "schedules")
    CourseClassScheduleResponse toCourseClassScheduleResponse(CourseClassResponse courseClassResponse, List<Schedule> schedules);

}
