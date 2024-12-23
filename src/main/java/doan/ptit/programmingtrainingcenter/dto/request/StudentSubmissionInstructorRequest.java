package doan.ptit.programmingtrainingcenter.dto.request;


import doan.ptit.programmingtrainingcenter.entity.StudentSubmission;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)

public class StudentSubmissionInstructorRequest {
    String submissionId;
    BigDecimal score;
    String feedBack;
    StudentSubmission.Status status;


}
