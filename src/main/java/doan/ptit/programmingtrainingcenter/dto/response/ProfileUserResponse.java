package doan.ptit.programmingtrainingcenter.dto.response;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileUserResponse {
    String fullName;

    String email;

    String gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date birthDate;

    String phoneNumber;

    String address;

    String profilePicture;

    String bio;
}
