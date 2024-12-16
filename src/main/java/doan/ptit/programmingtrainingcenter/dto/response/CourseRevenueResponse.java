package doan.ptit.programmingtrainingcenter.dto.response;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CourseRevenueResponse {
    private String courseId;
    private String courseName;
    private BigDecimal totalRevenue;
}
