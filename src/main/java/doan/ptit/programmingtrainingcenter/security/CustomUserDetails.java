package doan.ptit.programmingtrainingcenter.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    @Getter
    private final String id; // Thêm id
    @Getter
    private final String profilePicture;
    private final String email;
    private final String password;
    private final boolean isLocked;  // Thêm thuộc tính khóa tài khoản
    private final boolean isEnabled; // Thêm thuộc tính kích hoạt tài khoản
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(String id,String profilePicture, String email, String password, boolean isLocked, boolean isEnabled,
                             Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.profilePicture = profilePicture;
        this.password = password;
        this.isLocked = isLocked;
        this.isEnabled = isEnabled;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked; // Nếu isLocked là true, tài khoản sẽ bị khóa
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled; // Trả về trạng thái kích hoạt tài khoản
    }
}
