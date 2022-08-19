package general.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain configureForms(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .authorizeRequests(authz -> authz
                        .antMatchers("/addSchool").access("hasRole('ROLE_ADMIN')")
                        .antMatchers("/adduser", "/addClass", "/setClassForStudent", "/addClassForTeacher").access("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
                        .antMatchers("/aboutSchool").access("hasRole('ROLE_OPERATOR')")
                        .antMatchers("/addTask/**", "/checkAnswer/**", "/files", "/tasksOfClass/**").access("hasRole('ROLE_TEACHER')")
                        .antMatchers("/addAnswer").access("hasRole('ROLE_STUDENT')")
                        .antMatchers("/js/**", "/css/**").permitAll()
                        .antMatchers("/").authenticated()
                ).formLogin().loginPage("/login")
                .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login");
        return http.build();

    }

    @Bean
    @Order(1)
    public SecurityFilterChain configureApiLogin(HttpSecurity http) throws Exception {
        http.antMatcher("/api/**").cors()
                .and().csrf().disable()
                .authorizeHttpRequests((authz) -> authz
                        .anyRequest().authenticated()
                ).httpBasic()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();
    }
}