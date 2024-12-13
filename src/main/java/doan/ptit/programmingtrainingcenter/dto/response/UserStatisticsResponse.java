package doan.ptit.programmingtrainingcenter.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStatisticsResponse {
    private long totalStudents;
    private long totalInstructors;
}
