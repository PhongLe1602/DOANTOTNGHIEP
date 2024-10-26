package doan.ptit.programmingtrainingcenter.dto.request;


import lombok.Data;

@Data
public class RegisterRequest {


    private String fullName;
    private String email;
    private String phoneNumber;
    private String password;
    private String confirmPassword;

//    private String address;
//    private String city;
//    private String state;
//    private String zip;
//    private String country;
//    private String gender;
//    private String birthDate;
}
