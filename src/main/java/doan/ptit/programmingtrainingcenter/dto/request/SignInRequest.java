package doan.ptit.programmingtrainingcenter.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignInRequest {

    @NotBlank(message = "EmaiL not null")
    private String email;
    @NotBlank(message = "Password not null")
    private String password;
}
