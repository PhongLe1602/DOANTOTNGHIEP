package doan.ptit.programmingtrainingcenter.service.impl;



import doan.ptit.programmingtrainingcenter.dto.request.RecurringScheduleRequest;
import doan.ptit.programmingtrainingcenter.dto.request.ScheduleRequest;
import doan.ptit.programmingtrainingcenter.dto.response.CourseClassResponse;
import doan.ptit.programmingtrainingcenter.dto.response.CourseClassScheduleResponse;
import doan.ptit.programmingtrainingcenter.dto.response.ScheduleResponse;
import doan.ptit.programmingtrainingcenter.entity.Course;
import doan.ptit.programmingtrainingcenter.entity.CourseClass;
import doan.ptit.programmingtrainingcenter.entity.Schedule;
import doan.ptit.programmingtrainingcenter.entity.User;
import doan.ptit.programmingtrainingcenter.exception.ConflictException;
import doan.ptit.programmingtrainingcenter.mapper.CourseClassMapper;
import doan.ptit.programmingtrainingcenter.mapper.ScheduleMapper;
import doan.ptit.programmingtrainingcenter.repository.CourseClassRepository;
import doan.ptit.programmingtrainingcenter.repository.CourseRepository;
import doan.ptit.programmingtrainingcenter.repository.ScheduleRepository;
import doan.ptit.programmingtrainingcenter.repository.UserRepository;
import doan.ptit.programmingtrainingcenter.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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

    @Autowired
    private CourseClassMapper courseClassMapper;

    @Autowired
    private CourseClassRepository courseClassRepository;

    @Override
    public Schedule createSchedule(ScheduleRequest scheduleRequest) {
        CourseClass courseClass = courseClassRepository.findById(scheduleRequest.getCourseClassId()).
                orElseThrow(() -> new RuntimeException("Course Class Not Found"));



        Schedule schedule = scheduleMapper.toEntity(scheduleRequest,courseClass);

        return scheduleRepository.save(schedule);

    }

    @Override
    public ScheduleResponse getSchedule(String scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new RuntimeException("Schedule Not Found"));

        return scheduleMapper.toResponse(schedule);
    }

    @Override
    public List<ScheduleResponse> getSchedulesByCourse(String courseClassId) {
        List<Schedule> schedules = scheduleRepository.findByCourseClassId(courseClassId);

        return scheduleMapper.toResponseList(schedules);
    }

    @Override
    public List<Schedule> createRecurringSchedules(RecurringScheduleRequest request) {
        CourseClass courseClass = courseClassRepository.findById(request.getCourseClassId())
                .orElseThrow(() -> new RuntimeException("Course Class Not Found"));

        List<Schedule> schedulesOfInstructors = scheduleRepository.findSchedulesByInstructorId(courseClass.getInstructor().getId());

        List<LocalDate> repeatDates = getRepeatDates(
                request.getStartDate(),
                request.getEndDate(),
                request.getDaysOfWeek()
        );

        // Validate all proposed schedules before creating any
        for (LocalDate date : repeatDates) {
            LocalDateTime newStartDateTime = LocalDateTime.of(date, request.getStartTime());
            LocalDateTime newEndDateTime = newStartDateTime.plusMinutes(request.getDuration());

            for (Schedule existingSchedule : schedulesOfInstructors) {
                // Convert existing schedule times to LocalDateTime
                LocalDateTime existingStartDateTime = existingSchedule.getStartTime().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                LocalDateTime existingEndDateTime = existingSchedule.getEndTime().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();

                // Check for any type of overlap
                if (isTimeOverlap(newStartDateTime, newEndDateTime, existingStartDateTime, existingEndDateTime)) {
                    throw new ConflictException(String.format(
                            "Lịch học bị trùng vào ngày %s: Ca mới (%s - %s) trùng với ca hiện tại (%s - %s)",
                            date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                            request.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                            newEndDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                            existingStartDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                            existingEndDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                    ));
                }
            }
        }

        // If no conflicts found, create the schedules
        List<Schedule> schedules = new ArrayList<>();
        for (LocalDate date : repeatDates) {
            Schedule schedule = Schedule.builder()
                    .courseClass(courseClass)
                    .sessionType(request.getSessionType())
                    .sessionDate(convertToDate(date))
                    .startTime(convertToDateTime(date, request.getStartTime()))
                    .endTime(calculateEndTime(date, request.getStartTime(), request.getDuration()))
                    .duration(request.getDuration())
                    .description(request.getDescription())
                    .onlineLink(request.getOnlineLink())
                    .location(request.getLocation())
                    .build();

            schedules.add(schedule);
        }

        List<String> studyDays = convertToStudyDays(request.getDaysOfWeek());
        String studyTime = formatStudyTime(request.getStartTime(), request.getDuration());
        String studyDaysString = String.join(", ", studyDays);

        courseClass.setTotalSessions(schedules.size());
        courseClass.setStartDate(convertToDate(request.getStartDate()));
        courseClass.setEndDate(convertToDate(request.getEndDate()));
        courseClass.setStudyTime(studyTime);
        courseClass.setStudyDays(studyDaysString);
        courseClass.setStatus(CourseClass.Status.ACTIVE);
        courseClassRepository.save(courseClass);

        return scheduleRepository.saveAll(schedules);
    }

    @Override
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    @Override
    public List<ScheduleResponse> getSchedulesByUser(String userId) {
        List<Schedule> schedules = scheduleRepository.findSchedulesByUserId(userId);
        return scheduleMapper.toResponseList(schedules);
    }

    @Override
    public List<ScheduleResponse> getSchedulesByInstructor(String instructorId) {
        List<Schedule> schedules = scheduleRepository.findSchedulesByInstructorId(instructorId);
        return scheduleMapper.toResponseList(schedules);
    }

    @Override
    public CourseClassScheduleResponse getSchedulesOfCourseClass(String courseClassId) {
        // Lấy thông tin lớp học
        CourseClass courseClass = courseClassRepository.findById(courseClassId)
                .orElseThrow(() -> new RuntimeException("Course class not found"));

        CourseClassResponse courseClassResponse = courseClassMapper.toResponse(courseClass);

        // Lấy danh sách lịch học
        List<Schedule> schedules = scheduleRepository.findByCourseClassId(courseClassId);

        // Sử dụng mapper để ánh xạ
        return scheduleMapper.toCourseClassScheduleResponse(courseClassResponse, schedules);
    }

    @Override
    public List<Schedule> updateRecurringSchedules(RecurringScheduleRequest request) {

        CourseClass courseClass = courseClassRepository.findById(request.getCourseClassId())
                .orElseThrow(() -> new RuntimeException("Course Class Not Found"));

        // Lấy  tất cả lịch trình hiện có cho lớp khóa học này
        List<Schedule> existingSchedules = scheduleRepository.findByCourseClassId(request.getCourseClassId());

        // Lấy tất cả lịch trình của người hướng dẫn ngoại trừ lớp học này
        List<Schedule> otherInstructorSchedules = scheduleRepository.findSchedulesByInstructorId(courseClass.getInstructor().getId())
                .stream()
                .filter(schedule -> !schedule.getCourseClass().getId().equals(request.getCourseClassId()))
                .toList();

        // Tính ngày lịch trình mới
        List<LocalDate> newDates = getRepeatDates(
                request.getStartDate(),
                request.getEndDate(),
                request.getDaysOfWeek()
        );

        // Validate new schedules against other instructor schedules
        for (LocalDate date : newDates) {
            LocalDateTime newStartDateTime = LocalDateTime.of(date, request.getStartTime());
            LocalDateTime newEndDateTime = newStartDateTime.plusMinutes(request.getDuration());

            for (Schedule existingSchedule : otherInstructorSchedules) {
                LocalDateTime existingStartDateTime = existingSchedule.getStartTime().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                LocalDateTime existingEndDateTime = existingSchedule.getEndTime().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();

                if (isTimeOverlap(newStartDateTime, newEndDateTime, existingStartDateTime, existingEndDateTime)) {
                    throw new ConflictException(String.format(
                            "Lịch học bị trùng vào ngày %s: Ca mới (%s - %s) trùng với ca hiện tại (%s - %s)",
                            date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                            request.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                            newEndDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                            existingStartDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                            existingEndDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                    ));
                }
            }
        }

        // Delete existing schedules
        scheduleRepository.deleteAll(existingSchedules);

        // Create new schedules
        List<Schedule> newSchedules = new ArrayList<>();
        for (LocalDate date : newDates) {
            Schedule schedule = Schedule.builder()
                    .courseClass(courseClass)
                    .sessionType(request.getSessionType())
                    .sessionDate(convertToDate(date))
                    .startTime(convertToDateTime(date, request.getStartTime()))
                    .endTime(calculateEndTime(date, request.getStartTime(), request.getDuration()))
                    .duration(request.getDuration())
                    .description(request.getDescription())
                    .onlineLink(request.getOnlineLink())
                    .location(request.getLocation())
                    .build();

            newSchedules.add(schedule);
        }

        // Update course class information
        List<String> studyDays = convertToStudyDays(request.getDaysOfWeek());
        String studyTime = formatStudyTime(request.getStartTime(), request.getDuration());
        String studyDaysString = String.join(", ", studyDays);

        courseClass.setTotalSessions(newSchedules.size());
        courseClass.setStartDate(convertToDate(request.getStartDate()));
        courseClass.setEndDate(convertToDate(request.getEndDate()));
        courseClass.setStudyTime(studyTime);
        courseClass.setStudyDays(studyDaysString);
        courseClassRepository.save(courseClass);

        // Save new schedules
        return scheduleRepository.saveAll(newSchedules);
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

    private String formatStudyTime(LocalTime startTime, int duration) {
        LocalTime endTime = startTime.plusMinutes(duration);
        return startTime.format(DateTimeFormatter.ofPattern("HH:mm")) + " - " +
                endTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    private List<String> convertToStudyDays(List<Integer> daysOfWeek) {
        Map<Integer, String> vietnameseDays = Map.of(
                1, "Thứ 2",
                2, "Thứ 3",
                3, "Thứ 4",
                4, "Thứ 5",
                5, "Thứ 6",
                6, "Thứ 7",
                7, "Chủ nhật"
        );

        return daysOfWeek.stream()
                .map(vietnameseDays::get)
                .collect(Collectors.toList());
    }

    private boolean isTimeOverlap(
            LocalDateTime newStart,
            LocalDateTime newEnd,
            LocalDateTime existingStart,
            LocalDateTime existingEnd
    ) {
        return (newStart.isBefore(existingEnd) || newStart.isEqual(existingEnd)) &&
                (newEnd.isAfter(existingStart) || newEnd.isEqual(existingStart));
    }





}
