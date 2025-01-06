package doan.ptit.programmingtrainingcenter.controller;



import doan.ptit.programmingtrainingcenter.dto.request.RecurringScheduleRequest;
import doan.ptit.programmingtrainingcenter.dto.request.ScheduleRequest;
import doan.ptit.programmingtrainingcenter.dto.response.ApiListResponse;
import doan.ptit.programmingtrainingcenter.dto.response.CourseClassScheduleResponse;
import doan.ptit.programmingtrainingcenter.dto.response.ScheduleResponse;
import doan.ptit.programmingtrainingcenter.entity.Schedule;
import doan.ptit.programmingtrainingcenter.security.CustomUserDetails;
import doan.ptit.programmingtrainingcenter.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping
    Schedule createSchedule(@RequestBody ScheduleRequest scheduleRequest) {
        return scheduleService.createSchedule(scheduleRequest);
    }
    @GetMapping
    List<Schedule> getSchedules() {
        return scheduleService.getAllSchedules();
    }
    @GetMapping("/{id}")
    ScheduleResponse getSchedule(@PathVariable String id) {
        return scheduleService.getSchedule(id);
    }
    @GetMapping("/courses/{courseClassId}")
    List<ScheduleResponse> getScheduleByCourseId(@PathVariable String courseClassId) {
        return scheduleService.getSchedulesByCourse(courseClassId);
    }
    @PostMapping("/recurring")
    public ApiListResponse<Schedule> createRecurringSchedules(@RequestBody RecurringScheduleRequest recurringScheduleRequest) {
        List<Schedule> schedules = scheduleService.createRecurringSchedules(recurringScheduleRequest);

        if (schedules.isEmpty()) {
            return ApiListResponse.error(404, "Không tạo được lịch học nào", schedules);
        }

        return ApiListResponse.success(201, "Tạo lịch học thành công", schedules);
    }


    @PutMapping("/recurring")
    public ApiListResponse<Schedule> updateSchedulesRecurring(
                                                               @RequestBody RecurringScheduleRequest request) {
        List<Schedule> schedules = scheduleService.updateRecurringSchedules(request);

        if (schedules.isEmpty()) {
            return ApiListResponse.error(404, "Không cập nhật được lịch học nào", schedules);
        }

        return ApiListResponse.success(201, "Cập nhật lịch học thành công", schedules);
    }


    @GetMapping("/user")
    public List<ScheduleResponse> getSchedulesByUser() {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return scheduleService.getSchedulesByUser(currentUser.getId());
    }
    @GetMapping("/instructor")
    public List<ScheduleResponse> instructorSchedules() {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return scheduleService.getSchedulesByInstructor(currentUser.getId());
    }
    @GetMapping("/course-class/{courseClassId}")
    public CourseClassScheduleResponse getSchedulesByCourse(@PathVariable String courseClassId) {
        return scheduleService.getSchedulesOfCourseClass(courseClassId);
    }
}
