package org.woonkoh.capstonedraft.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

    String[] projectResources = new String[]{
            "/include/**", "/css/**", "/icons/**", "/img/**", "/js/**", "/layer/**", "/static/**"
    };

        @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers(projectResources).permitAll()
                        .requestMatchers("/", "/index", "/register", "/register/save","/products/**","/orders/**").permitAll()
                        .requestMatchers("/addProductToOrder").authenticated()
//                        .requestMatchers(HttpMethod.DELETE, "/users/**").permitAll()

                        .anyRequest().authenticated()

                )
                .formLogin(
                        form -> form
                                .loginPage("/login")
                                .permitAll()
                                .defaultSuccessUrl("/")
                                .failureUrl("/login?error=true")
                                .usernameParameter("username")
                                .passwordParameter("password")
                )
                .logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                                .logoutSuccessUrl("/login?logout")
                );

        return http.build();
    }



}