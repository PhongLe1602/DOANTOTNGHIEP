package doan.ptit.programmingtrainingcenter.service;


import doan.ptit.programmingtrainingcenter.dto.response.UserStatisticsResponse;
import org.springframework.stereotype.Service;

@Service
public interface StatisticsService {
    UserStatisticsResponse getUserStatistics();
}
