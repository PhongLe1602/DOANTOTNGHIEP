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

    private final String [] PUBLIC_ENDPOINTS = {"/api/course-topic/**","/api/users/home/instructors","/api/consults/**","/api/student-submissions/**","/api/assignments/**","/api/statistics/**","/api/notifications/**","/api/attendance-session/**","/api/orders/**","/api/attendances/**","/api/classes/**","/api/review/**","/api/auth/**" ,"/api/categories/**","/api/courses/**","/api/sections/**","/api/lessons/**","/api/enrollments/**","/api/payment-method/**","/api/payments/**","/api/schedule/**","/api/carts/**"};

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
                        // Phân quyền quản lý người dùng
                        .requestMatchers("/api/users").hasAuthority("MANAGE_USERS")

                        // Phân quyền quản lý khóa học
                        .requestMatchers(HttpMethod.POST, "/api/courses/**").hasAuthority("MANAGE_COURSES")
                        .requestMatchers(HttpMethod.POST, "/api/topics/**").hasAuthority("MANAGE_COURSES")

                        // Phân quyền quản lý lộ trình
                        .requestMatchers(HttpMethod.POST, "/api/sections/**").hasAuthority("MANAGE_SECTIONS")

                        // Phân quyền quản lý bài học
                        .requestMatchers(HttpMethod.POST, "/api/lessons/**").hasAuthority("MANAGE_LESSONS")

                        // Phân quyền quản lý quyền và vai trò
                        .requestMatchers(HttpMethod.POST, "/api/roles/**").hasAuthority("MANAGE_ROLES")
                        .requestMatchers(HttpMethod.POST, "/api/permissions/**").hasAuthority("MANAGE_PERMISSIONS")

                        //Phân quyền quản lý đơn hàng
                        .requestMatchers(HttpMethod.POST, "/api/orders/**").hasAuthority("MANAGE_ORDERS")
                        .requestMatchers(HttpMethod.GET, "/api/orders").hasAuthority("MANAGE_ORDERS")

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