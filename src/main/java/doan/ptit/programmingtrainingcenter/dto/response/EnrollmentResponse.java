package doan.ptit.programmingtrainingcenter.dto.response;


import doan.ptit.programmingtrainingcenter.entity.Enrollment;
import doan.ptit.programmingtrainingcenter.entity.OrderItem;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnrollmentResponse {
    String id;
    UserResponse user;
    CoursesResponse courses;
    OrderItem orderItem;
    Date enrollmentDate;
    Enrollment.Status status;
    BigDecimal progress;
    Date lastAccessed;

}
