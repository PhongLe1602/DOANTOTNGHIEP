package doan.ptit.programmingtrainingcenter.controller;



import doan.ptit.programmingtrainingcenter.dto.request.RecurringScheduleRequest;
import doan.ptit.programmingtrainingcenter.dto.request.ScheduleRequest;
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
    public List<Schedule> createRecurringSchedules(@RequestBody RecurringScheduleRequest recurringScheduleRequest) {
        return scheduleService.createRecurringSchedules(recurringScheduleRequest);
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
}
