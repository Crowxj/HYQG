package com.kyhorizon.hyqgsbs.config;

import com.kyhorizon.hyqgsbs.entity.SysAccount;
import com.kyhorizon.hyqgsbs.filter.JwtAuthenticationFilter;
import com.kyhorizon.hyqgsbs.mapper.SysAccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.kyhorizon.hyqgsbs.common.util.JwtUtil;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SysAccountMapper sysAccountMapper;
    private final JwtUtil jwtUtil; //注入JwtUtil

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        // ✅ 手动new Jwt过滤器，不再从容器注入，打破循环依赖
        JwtAuthenticationFilter jwtAuthFilter = new JwtAuthenticationFilter(jwtUtil, userDetailsService());

        http.csrf(csrf->csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            SysAccount account = sysAccountMapper.selectByUsername(username);
            if(account == null){
                throw new UsernameNotFoundException("账号不存在");
            }
            if(account.getStatus() != 1){
                throw new LockedException("账号已被禁用");
            }
            return User.withUsername(account.getUsername())
                    .password(account.getPassword())
                    .roles("USER")
                    .build();
        };
    }
}
