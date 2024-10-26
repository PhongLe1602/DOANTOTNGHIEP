package doan.ptit.programmingtrainingcenter.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class EnrollmentRequest {
    private String userId;
    private String courseId;
    private String status;
    private BigDecimal progress;

}
