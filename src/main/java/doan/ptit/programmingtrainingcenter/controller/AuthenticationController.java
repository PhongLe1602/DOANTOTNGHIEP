package doan.ptit.programmingtrainingcenter.controller;


import doan.ptit.programmingtrainingcenter.dto.request.ChangePasswordRequest;
import doan.ptit.programmingtrainingcenter.dto.request.RegisterRequest;
import doan.ptit.programmingtrainingcenter.dto.request.SignInRequest;
import doan.ptit.programmingtrainingcenter.dto.response.AuthResponse;
import doan.ptit.programmingtrainingcenter.dto.response.TokenResponse;
import doan.ptit.programmingtrainingcenter.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/auth")
@Validated
@Slf4j

public class AuthenticationController {

    @Autowired
    private  AuthenticationService authenticationService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid SignInRequest signInRequest) {
        TokenResponse response = authenticationService.authenticate(signInRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(HttpServletRequest request) {
        TokenResponse response = authenticationService.refresh(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        AuthResponse response = authenticationService.register(registerRequest);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/change-password")
    public ResponseEntity<AuthResponse> changePassword (@RequestBody ChangePasswordRequest changePasswordRequest) {
        AuthResponse response = authenticationService.changePassword(changePasswordRequest);

        return ResponseEntity.ok(response);
    }
}
