package doan.ptit.programmingtrainingcenter.service.impl;


import doan.ptit.programmingtrainingcenter.dto.response.CourseRevenueResponse;
import doan.ptit.programmingtrainingcenter.dto.response.EnrollmentResponse;
import doan.ptit.programmingtrainingcenter.dto.response.InstructorStatisticsResponse;
import doan.ptit.programmingtrainingcenter.dto.response.UserStatisticsResponse;
import doan.ptit.programmingtrainingcenter.entity.CourseClass;
import doan.ptit.programmingtrainingcenter.entity.Enrollment;
import doan.ptit.programmingtrainingcenter.mapper.EnrollmentMapper;
import doan.ptit.programmingtrainingcenter.repository.*;
import doan.ptit.programmingtrainingcenter.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private EnrollmentMapper enrollmentMapper;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private CourseClassRepository courseClassRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Override
    public UserStatisticsResponse getUserStatistics() {
        long totalStudents = userRepository.countStudents();
        long totalInstructors = userRepository.countInstructors();


        return UserStatisticsResponse.builder()
                .totalInstructors(totalInstructors)
                .totalStudents(totalStudents)
                .build();
    }

    @Override
    public BigDecimal getRevenueStatistics(String fromDate, String toDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date from = dateFormat.parse(fromDate);
        Date to = dateFormat.parse(toDate);

        return paymentRepository.calculateCompletedRevenueInRange(from, to);
    }

    @Override
    public List<CourseRevenueResponse> getRevenueByCourse(String fromDate, String toDate) throws ParseException {
        // Parse String to Date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date from = dateFormat.parse(fromDate);
        Date to = dateFormat.parse(toDate);

        // Fetch data from repository
        List<Object[]> results = paymentRepository.calculateRevenueByCourse(from, to);

        return results.stream()
                .map(row -> CourseRevenueResponse.builder()
                        .courseId((String) row[0])
                        .courseName((String) row[1])
                        .totalRevenue((BigDecimal) row[2])
                        .build()
                )
                .collect(Collectors.toList());

    }

    @Override
    public List<EnrollmentResponse> getTopNewestEnrollments() {
        Pageable topThree = PageRequest.of(0, 3);
        List<Enrollment> newestEnrollments = enrollmentRepository.findTop3NewestEnrollments(topThree);


        return newestEnrollments.stream()
                .map(enrollmentMapper::toResponse)
                .toList();
    }

    @Override
    public InstructorStatisticsResponse getInstructorStatistics(String instructorId) {
        LocalDateTime now = LocalDateTime.now();
        Date startDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(now.plusDays(7).atZone(ZoneId.systemDefault()).toInstant());

        // Count schedules for the next 7 days
        long weeklySchedules = scheduleRepository.countByInstructorAndDateRange(
                instructorId, startDate, endDate);

        // Count active course classes
        long activeCourseClasses = courseClassRepository.countByInstructorIdAndStatus(
                instructorId, CourseClass.Status.ACTIVE);

        // Count total assignments
        long totalAssignments = assignmentRepository.countByInstructorId(instructorId);

        return InstructorStatisticsResponse.builder()
                .totalSchedulesThisWeek(weeklySchedules)
                .totalActiveCourseClasses(activeCourseClasses)
                .totalAssignments(totalAssignments)
                .build();
    }

}
