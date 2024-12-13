package doan.ptit.programmingtrainingcenter.service.impl;


import doan.ptit.programmingtrainingcenter.dto.response.UserStatisticsResponse;
import doan.ptit.programmingtrainingcenter.repository.UserRepository;
import doan.ptit.programmingtrainingcenter.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserStatisticsResponse getUserStatistics() {
        long totalStudents = userRepository.countStudents();
        long totalInstructors = userRepository.countInstructors();


        return UserStatisticsResponse.builder()
                .totalInstructors(totalInstructors)
                .totalStudents(totalStudents)
                .build();
    }
}
