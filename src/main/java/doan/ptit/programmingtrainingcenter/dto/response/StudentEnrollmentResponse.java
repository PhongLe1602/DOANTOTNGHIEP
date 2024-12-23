package doan.ptit.programmingtrainingcenter.dto.response;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentEnrollmentResponse {
     String id;
     String name;
     String email;
     String phone;
     int totalEnrollments;
     List<EnrollmentResponse> enrollments;
}
