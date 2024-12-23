package doan.ptit.programmingtrainingcenter.service.impl;


import doan.ptit.programmingtrainingcenter.dto.request.ChangePasswordRequest;
import doan.ptit.programmingtrainingcenter.dto.request.RegisterRequest;
import doan.ptit.programmingtrainingcenter.dto.request.SignInRequest;
import doan.ptit.programmingtrainingcenter.dto.response.AuthResponse;
import doan.ptit.programmingtrainingcenter.dto.response.TokenResponse;
import doan.ptit.programmingtrainingcenter.entity.Role;
import doan.ptit.programmingtrainingcenter.entity.User;
import doan.ptit.programmingtrainingcenter.repository.RoleRepository;
import doan.ptit.programmingtrainingcenter.repository.UserRepository;
import doan.ptit.programmingtrainingcenter.security.CustomUserDetails;
import doan.ptit.programmingtrainingcenter.security.CustomUserDetailsService;
import doan.ptit.programmingtrainingcenter.service.AuthenticationService;
import doan.ptit.programmingtrainingcenter.service.EmailService;
import doan.ptit.programmingtrainingcenter.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;



@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final RoleRepository roleRepository;

    @Value("${app.frontend.url}")
    private String fondEndUrl;

    public TokenResponse authenticate(SignInRequest signInRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));
        var userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(signInRequest.getEmail());
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(userDetails.getId())
                .profilePicture(userDetails.getProfilePicture())
                .roles(roles)
                .build();
    }

    @Override
    public TokenResponse refresh(HttpServletRequest request) {
        String token = request.getHeader("x-token");
        if(token == null) {
            System.out.println("token is null");
        }
        // lay user ra tu token
        final String useName = jwtService.extractUsername(token);
        System.out.println("Username: " + useName);
        //check voi database
        var userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(useName);

        if(!jwtService.validateToken(token, userDetails)) {
            throw new BadCredentialsException("Invalid token");
        }

        String accessToken = jwtService.generateToken(userDetails);



        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(token)
                .userId(userDetails.getId())
                .build();
    }

    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        if(userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        Role studentRole = roleRepository.findByName("STUDENT")
                .orElseThrow(() -> new RuntimeException("Role STUDENT not found"));
        String token = jwtService.generateActivationToken(registerRequest.getEmail());

        String activationLink = fondEndUrl + "/activate-account?token=" + token;

        User user = new User();
        user.setFullName(registerRequest.getFullName());
        user.setEmail(registerRequest.getEmail());
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setIsLocked(false);
        user.setIsEnabled(false);
        user.setRoles(Collections.singletonList(studentRole));
        userRepository.save(user);
        emailService.sendEmail(user.getEmail(),"Activate your account","Please click the following link to activate your account: " + activationLink);
        return AuthResponse.builder()
                .message("Đăng ký thành công")
                .status("success")
                .timestamp(new Date())
                .build();

    }

    @Override
    public AuthResponse changePassword(ChangePasswordRequest changePasswordRequest) {
        // Lấy thông tin người dùng hiện tại (dựa vào security context)
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // Kiểm tra xem mật khẩu hiện tại có đúng không
        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        // Kiểm tra xem mật khẩu mới có khác mật khẩu hiện tại không
        if (passwordEncoder.matches(changePasswordRequest.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException("New password must be different from the current password");
        }

        // Mã hóa mật khẩu mới và cập nhật cho người dùng
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);



        return AuthResponse.builder()
                .status("success")
                .message("Mat khau da duoc cap nhat")
                .timestamp(new Date())
                .build();
    }

    @Override
    public AuthResponse active(String token) {
        if (jwtService.isTokenExpired(token)) {
            throw new BadCredentialsException("Invalid token");
        }
        String email = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found"));
        if (user.getIsEnabled()) {
            throw new RuntimeException("Tài khoản đã được kích hoạt trước đó.");
        }
        user.setIsEnabled(true);
        userRepository.save(user);
        return AuthResponse.builder()
                .status("success")
                .message("Tai khoan cua ban da duoc kich hoat")
                .timestamp(new Date())
                .build();
    }

    @Override
    public TokenResponse adminLogin(SignInRequest signInRequest) {

        Authentication authenticationAdmin = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword())
        );

        var userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(signInRequest.getEmail());

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        if (!roles.contains("ROLE_ADMIN") && !roles.contains("ROLE_INSTRUCTOR") ) {
            throw new AccessDeniedException("Bạn không có quyền");
        }

        // Generate tokens
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        // Create response
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(userDetails.getId())
                .profilePicture(userDetails.getProfilePicture())
                .roles(roles)
                .build();
    }



}
