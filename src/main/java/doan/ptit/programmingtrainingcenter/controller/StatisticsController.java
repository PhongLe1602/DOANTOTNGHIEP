package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.response.CourseRevenueResponse;
import doan.ptit.programmingtrainingcenter.dto.response.EnrollmentResponse;
import doan.ptit.programmingtrainingcenter.dto.response.InstructorStatisticsResponse;
import doan.ptit.programmingtrainingcenter.dto.response.UserStatisticsResponse;
import doan.ptit.programmingtrainingcenter.entity.Enrollment;
import doan.ptit.programmingtrainingcenter.security.CustomUserDetails;
import doan.ptit.programmingtrainingcenter.service.EnrollmentService;
import doan.ptit.programmingtrainingcenter.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/users")
    public UserStatisticsResponse getUserStatistics() {

        return statisticsService.getUserStatistics();
    }
    @GetMapping("/revenue")
    public ResponseEntity<?> getCompletedRevenue(@RequestParam("from") String fromDate, @RequestParam("to") String toDate) throws ParseException {
        BigDecimal revenue = statisticsService.getRevenueStatistics(fromDate, toDate);
        return ResponseEntity.ok(Map.of("revenue", revenue));
    }
    @GetMapping("/total-revenue")
    public ResponseEntity<?> getTotalRevenue() {
        BigDecimal totalRevenue = statisticsService.totalRevenue();
        return ResponseEntity.ok(Map.of("totalRevenue", totalRevenue));
    }

    @GetMapping("/total-course")
    public ResponseEntity<?> getTotalCourse() {
        Long total = statisticsService.totalCourse();
        return ResponseEntity.ok(Map.of("totalCourse", total));
    }

    @GetMapping("/revenue-by-course")
    public ResponseEntity<List<CourseRevenueResponse>> getRevenueByCourse(
            @RequestParam("from") String fromDate,
            @RequestParam("to") String toDate) throws ParseException {
        List<CourseRevenueResponse> revenues = statisticsService.getRevenueByCourse(fromDate, toDate);
        return ResponseEntity.ok(revenues);
    }
    @GetMapping("/newest")
    public ResponseEntity<List<EnrollmentResponse>> getTop3NewestEnrollments() {
        List<EnrollmentResponse> newestEnrollments = statisticsService.getTopNewestEnrollments();
        return ResponseEntity.ok(newestEnrollments);
    }
    @GetMapping("/instructor")
    public ResponseEntity<InstructorStatisticsResponse> getInstructorStatistics() {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(statisticsService.getInstructorStatistics(currentUser.getId()));
    }
    @GetMapping("/enrollments")
    public ResponseEntity<Map<String, Object>> getEnrollmentStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        // Thêm các thống kê vào response
        statistics.put("totalStudents", enrollmentService.getTotalStudentsEnrolled());
        statistics.put("studyingCount", enrollmentService.getStudyingCount());
        statistics.put("completedCount", enrollmentService.getCompletedCount());
        statistics.put("pendingCount", enrollmentService.getPendingCount());

        return ResponseEntity.ok(statistics);
    }
}
