package com.kyhorizon.hyqgsbs.config;

import com.kyhorizon.hyqgsbs.entity.SysAccount;
import com.kyhorizon.hyqgsbs.entity.SysAccountSystem;
import com.kyhorizon.hyqgsbs.filter.JwtAuthenticationFilter;
import com.kyhorizon.hyqgsbs.mapper.SysAccountMapper;
import com.kyhorizon.hyqgsbs.mapper.SysAccountSystemMapper;
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
    // 新增注入系统账号mapper
    private final SysAccountSystemMapper sysAccountSystemMapper;
    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        JwtAuthenticationFilter jwtAuthFilter = new JwtAuthenticationFilter(jwtUtil, userDetailsService());

        http.csrf(csrf->csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/dashboard/**").permitAll()
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
            // 第一步：查询普通账号表 sys_account
            SysAccount normalAccount = sysAccountMapper.selectByUsername(username);
            if(normalAccount != null){
                // 普通账号需要校验status状态
                if(normalAccount.getStatus() != 1){
                    throw new LockedException("账号已被禁用");
                }
                return User.withUsername(normalAccount.getUsername())
                        .password(normalAccount.getPassword())
                        .roles("USER")
                        .build();
            }

            // 第二步：普通账号不存在，查询系统内置账号 sys_account_system
            SysAccountSystem sysAccount = sysAccountSystemMapper.selectByUsername(username);
            if(sysAccount != null){
                // 系统账号没有status字段，直接返回
                return User.withUsername(sysAccount.getUsername())
                        .password(sysAccount.getPassword())
                        .roles("ADMIN") // 系统账号给ADMIN角色，可自行调整
                        .build();
            }

            // 两张表都查不到，抛出账号不存在
            throw new UsernameNotFoundException("账号不存在");
        };
    }
}
