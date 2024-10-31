package doan.ptit.programmingtrainingcenter.controller;



import doan.ptit.programmingtrainingcenter.dto.request.ScheduleRequest;
import doan.ptit.programmingtrainingcenter.dto.response.ScheduleResponse;
import doan.ptit.programmingtrainingcenter.entity.Schedule;
import doan.ptit.programmingtrainingcenter.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping("/{id}")
    ScheduleResponse getSchedule(@PathVariable String id) {
        return scheduleService.getSchedule(id);
    }
    @GetMapping("/courses/{courseId}")
    List<ScheduleResponse> getScheduleByCourseId(@PathVariable String courseId) {
        return scheduleService.getSchedulesByCourse(courseId);
    }
}
