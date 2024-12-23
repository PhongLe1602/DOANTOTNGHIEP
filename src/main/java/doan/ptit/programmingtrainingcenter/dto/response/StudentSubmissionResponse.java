package doan.ptit.programmingtrainingcenter.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentSubmissionResponse {
    String id;
    UserResponse student;
    String fileUrl;
    Date submissionDate;
    String status;
    BigDecimal score;
    String feedback;
}
