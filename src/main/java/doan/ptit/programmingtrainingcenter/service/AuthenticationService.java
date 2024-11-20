package doan.ptit.programmingtrainingcenter.service;


import doan.ptit.programmingtrainingcenter.dto.request.ChangePasswordRequest;
import doan.ptit.programmingtrainingcenter.dto.request.RegisterRequest;
import doan.ptit.programmingtrainingcenter.dto.request.SignInRequest;
import doan.ptit.programmingtrainingcenter.dto.response.AuthResponse;
import doan.ptit.programmingtrainingcenter.dto.response.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
    TokenResponse authenticate(SignInRequest signInRequest);

    TokenResponse refresh(HttpServletRequest token);

    AuthResponse register(RegisterRequest registerRequest);

    AuthResponse changePassword(ChangePasswordRequest changePasswordRequest);

    AuthResponse active(String token);

}
