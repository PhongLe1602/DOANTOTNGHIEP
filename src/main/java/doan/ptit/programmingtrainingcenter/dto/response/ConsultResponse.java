package doan.ptit.programmingtrainingcenter.dto.response;


import doan.ptit.programmingtrainingcenter.entity.Consult;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsultResponse {
    String email;
    String fullName;
    Consult.Status status;
    String phoneNumber;
    String requestMessage;
    UserResponse consultant;
}
