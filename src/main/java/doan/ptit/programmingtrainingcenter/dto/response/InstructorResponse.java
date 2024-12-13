package doan.ptit.programmingtrainingcenter.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InstructorResponse {
    String fullName;
    String email;
    String profilePicture;
}
