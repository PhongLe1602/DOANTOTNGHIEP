package doan.ptit.programmingtrainingcenter.service;

import doan.ptit.programmingtrainingcenter.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface JwtService {
    String generateToken(UserDetails user);
    String extractUsername(String token);
    boolean validateToken(String token, UserDetails user);
    Boolean isTokenExpired(String token);
    String generateRefreshToken(UserDetails userDetails);
    String getUserIdFromToken(String token);
}
