package doan.ptit.programmingtrainingcenter.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    @NotEmpty
    String fullName;

    @NotEmpty
    String email;

    String gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date birthDate;

    String phoneNumber;

    String address;

//    MultipartFile profilePicture;

    String bio;

    @NotNull
    List<String> roleIds;
}
