package doan.ptit.programmingtrainingcenter.configuration;



import doan.ptit.programmingtrainingcenter.filter.PreFilter;
import doan.ptit.programmingtrainingcenter.security.CustomUserDetailsService;
import doan.ptit.programmingtrainingcenter.service.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final String [] PUBLIC_ENDPOINTS = {"/api/auth/**" ,"/api/categories/**","/api/courses/**","/api/sections/**","/api/lessons/**","/api/enrollments/**","/api/payment-method/**","/api/payments/**","/api/schedule/**","/api/carts/**","/api/roles/**","/api/permissions/**","/api/user-role/**","/api/role-permission/**"};

    private final  UserService userService;

    private final PreFilter preFilter;

    private final UserDetailsService userDetailsService;

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(@NotNull HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request ->
                request.requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/auth/change-password").hasAuthority("ROLE_STUDENT")
                        // Người dùng có quyền 'VIEW_USER' mới được xem thông tin người dùng
                        .requestMatchers(HttpMethod.POST, "/api/users/**").hasAuthority("MANAGE_USERS")
//                        .requestMatchers(HttpMethod.GET, "/api/courses/**").hasAuthority("MANAGE_COURSES")
                        .requestMatchers(HttpMethod.GET, "/api/topics/**").hasAuthority("MANAGE_COURSES")
                        .anyRequest().authenticated())
                        .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(provider()).addFilterBefore(preFilter, UsernamePasswordAuthenticationFilter.class)
        ;


        httpSecurity.cors(Customizer.withDefaults()).csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

//    @Bean
    public AuthenticationProvider provider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();

    }




}