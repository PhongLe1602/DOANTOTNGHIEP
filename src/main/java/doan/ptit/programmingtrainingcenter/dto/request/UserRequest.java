package doan.ptit.programmingtrainingcenter.dto.request;


import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    String fullName;

    String email;

    @Size(min = 6 , message = "PASSWORD_INVALID")
    String password;

    String gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date birthDate;

    String phoneNumber;

    String address;

    MultipartFile profilePicture;

    String bio;

}
