package doan.ptit.programmingtrainingcenter.dto.request;


import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileUserRequest {
    String fullName;

    String email;

    String gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date birthDate;

    String phoneNumber;

    String address;

    String bio;
}
