package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.response.CourseRevenueResponse;
import doan.ptit.programmingtrainingcenter.dto.response.UserStatisticsResponse;
import doan.ptit.programmingtrainingcenter.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/users")
    public UserStatisticsResponse getUserStatistics() {

        return statisticsService.getUserStatistics();
    }
    @GetMapping("/revenue")
    public ResponseEntity<?> getCompletedRevenue(@RequestParam("from") String fromDate, @RequestParam("to") String toDate) throws ParseException {
        BigDecimal revenue = statisticsService.getRevenueStatistics(fromDate, toDate);
        return ResponseEntity.ok(Map.of("revenue", revenue));
    }

    @GetMapping("/revenue-by-course")
    public ResponseEntity<List<CourseRevenueResponse>> getRevenueByCourse(
            @RequestParam("from") String fromDate,
            @RequestParam("to") String toDate) throws ParseException {
        List<CourseRevenueResponse> revenues = statisticsService.getRevenueByCourse(fromDate, toDate);
        return ResponseEntity.ok(revenues);
    }
}
