package doan.ptit.programmingtrainingcenter.service.impl;



import doan.ptit.programmingtrainingcenter.dto.request.RecurringScheduleRequest;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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

    @Override
    public List<Schedule> createRecurringSchedules(RecurringScheduleRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course Not Found"));

        User instructor = userRepository.findById(request.getInstructorId())
                .orElseThrow(() -> new RuntimeException("Instructor Not Found"));

        List<LocalDate> repeatDates = getRepeatDates(
                request.getStartDate(),
                request.getEndDate(),
                request.getDaysOfWeek()
        );

        List<Schedule> schedules = new ArrayList<>();
        for (LocalDate date : repeatDates) {
            Schedule schedule = Schedule.builder()
                    .course(course)
                    .sessionType(request.getSessionType())
                    .sessionDate(convertToDate(date)) // Ngày của buổi học
                    .startTime(convertToDateTime(date, request.getStartTime())) // Thời gian bắt đầu
                    .endTime(calculateEndTime(date, request.getStartTime(), request.getDuration())) // Tính thời gian kết thúc
                    .duration(request.getDuration())
                    .description(request.getDescription())
                    .instructor(instructor)
                    .onlineLink(request.getOnlineLink())
                    .location(request.getLocation())
                    .build();

            schedules.add(schedule);
        }

        return scheduleRepository.saveAll(schedules);
    }

    @Override
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    private List<LocalDate> getRepeatDates(LocalDate startDate, LocalDate endDate, List<Integer> daysOfWeek) {
        List<LocalDate> repeatDates = new ArrayList<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            if (daysOfWeek.contains(currentDate.getDayOfWeek().getValue())) {
                repeatDates.add(currentDate);
            }
            currentDate = currentDate.plusDays(1);
        }
        return repeatDates;
    }

    private Date convertToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private Date convertToDateTime(LocalDate date, LocalTime time) {
        return Date.from(LocalDateTime.of(date, time).atZone(ZoneId.systemDefault()).toInstant());
    }

    private Date calculateEndTime(LocalDate date, LocalTime startTime, int duration) {
        LocalDateTime endDateTime = LocalDateTime.of(date, startTime).plusMinutes(duration);
        return Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }



}
