package doan.ptit.programmingtrainingcenter.service.impl;


import doan.ptit.programmingtrainingcenter.dto.response.CourseRevenueResponse;
import doan.ptit.programmingtrainingcenter.dto.response.UserStatisticsResponse;
import doan.ptit.programmingtrainingcenter.repository.PaymentRepository;
import doan.ptit.programmingtrainingcenter.repository.UserRepository;
import doan.ptit.programmingtrainingcenter.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentRepository paymentRepository;

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
}
