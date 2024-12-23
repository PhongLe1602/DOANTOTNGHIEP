package doan.ptit.programmingtrainingcenter.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InstructorStatisticsResponse {
    private long totalSchedulesThisWeek;
    private long totalActiveCourseClasses;
    private long totalAssignments;
}
