package doan.ptit.programmingtrainingcenter.dto.request;


import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    String fullName;

    String email;

    @Size(min = 6 , message = "PASSWORD_INVALID")
    String password;

    String phoneNumber;

    String address;

    String profilePicture;

    String bio;

}
