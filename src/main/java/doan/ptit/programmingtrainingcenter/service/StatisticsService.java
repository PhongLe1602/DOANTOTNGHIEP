package doan.ptit.programmingtrainingcenter.service;


import doan.ptit.programmingtrainingcenter.dto.response.CourseRevenueResponse;
import doan.ptit.programmingtrainingcenter.dto.response.EnrollmentResponse;
import doan.ptit.programmingtrainingcenter.dto.response.UserStatisticsResponse;
import doan.ptit.programmingtrainingcenter.entity.Enrollment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

@Service
public interface StatisticsService {
    UserStatisticsResponse getUserStatistics();
    BigDecimal getRevenueStatistics(String fromDate, String toDate) throws ParseException;
    List<CourseRevenueResponse> getRevenueByCourse(String fromDate, String toDate) throws ParseException;
    List<EnrollmentResponse> getTopNewestEnrollments();
}
