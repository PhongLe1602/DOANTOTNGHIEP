package doan.ptit.programmingtrainingcenter.service.impl;



import doan.ptit.programmingtrainingcenter.dto.request.ScheduleRequest;
import doan.ptit.programmingtrainingcenter.dto.response.ScheduleResponse;
import doan.ptit.programmingtrainingcenter.entity.Course;
import doan.ptit.programmingtrainingcenter.entity.Schedule;
import doan.ptit.programmingtrainingcenter.entity.User;
import doan.ptit.programmingtrainingcenter.mapper.ScheduleMapper;
import doan.ptit.programmingtrainingcenter.repository.CourseRepository;
import doan.ptit.programmingtrainingcenter.repository.ScheduleRepository;
import doan.ptit.programmingtrainingcenter.repository.UserRepository;
import doan.ptit.programmingtrainingcenter.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Override
    public Schedule createSchedule(ScheduleRequest scheduleRequest) {
        Course course = courseRepository.findById(scheduleRequest.getCourseId()).orElseThrow(() -> new RuntimeException("Course Not Found"));

        User user = userRepository.findById(scheduleRequest.getInstructorId()).orElseThrow(() -> new RuntimeException("User Not Found"));

        Schedule schedule = scheduleMapper.toEntity(scheduleRequest,course,user);

        return scheduleRepository.save(schedule);

    }

    @Override
    public ScheduleResponse getSchedule(String scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new RuntimeException("Schedule Not Found"));

        return scheduleMapper.toResponse(schedule);
    }

    @Override
    public List<ScheduleResponse> getSchedulesByCourse(String courseId) {
        List<Schedule> schedules = scheduleRepository.findByCourseId(courseId);

        return scheduleMapper.toResponseList(schedules);
    }




}
