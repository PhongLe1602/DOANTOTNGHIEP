package doan.ptit.programmingtrainingcenter.service;


import doan.ptit.programmingtrainingcenter.dto.request.RecurringScheduleRequest;
import doan.ptit.programmingtrainingcenter.dto.request.ScheduleRequest;
import doan.ptit.programmingtrainingcenter.dto.response.ScheduleResponse;
import doan.ptit.programmingtrainingcenter.entity.Schedule;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ScheduleService {
    Schedule createSchedule(ScheduleRequest scheduleRequest);
    ScheduleResponse getSchedule(String scheduleId);

    List<ScheduleResponse> getSchedulesByCourse(String courseId);
    List<Schedule> createRecurringSchedules(RecurringScheduleRequest request);
    List<Schedule> getAllSchedules();
    List<ScheduleResponse> getSchedulesByUser(String userId);
}
