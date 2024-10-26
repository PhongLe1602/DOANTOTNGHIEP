package doan.ptit.programmingtrainingcenter.security;

import doan.ptit.programmingtrainingcenter.entity.User;
import doan.ptit.programmingtrainingcenter.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Tìm kiếm user theo email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Lấy các quyền từ vai trò và quyền hạn (Role và Permission)
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        // Thêm các quyền từ vai trò (Role)
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));  // Role với tiền tố 'ROLE_'

            // Thêm các quyền (Permission) cho mỗi Role
            role.getPermissions().forEach(permission -> {
                authorities.add(new SimpleGrantedAuthority(permission.getName()));  // Quyền không cần tiền tố
            });
        });

        log.info("User Authorities: {}", authorities);
        log.info("User Details: {}", user);
        // Trả về CustomUserDetails (bao gồm cả id của user)
        return new CustomUserDetails(user.getId(),user.getProfilePicture(), user.getEmail(), user.getPassword() ,user.getIsLocked(), user.getIsEnabled(), authorities);
    }
}
