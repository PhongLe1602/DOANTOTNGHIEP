package doan.ptit.programmingtrainingcenter.dto.request;


import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String oldPassword;
    private String confirmPassword;
    private String newPassword;
}
