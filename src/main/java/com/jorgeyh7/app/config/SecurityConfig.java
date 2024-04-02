package com.jorgeyh7.app.config;

import com.jorgeyh7.app.service.UserDetailServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity//configuraciones con anotaciones
public class SecurityConfig {


/*    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf(csrf->csrf.disable())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http->{
                        http.requestMatchers(HttpMethod.GET,"auth/get").permitAll();
                        http.requestMatchers(HttpMethod.POST, "auth/post").hasAnyAuthority("CREATE","READ");
                        http.requestMatchers(HttpMethod.PATCH, "auth/patch").hasAuthority("REFACTOR");
                         http.requestMatchers(HttpMethod.PATCH, "auth/patch").hasRole("ADMIN");
                        http.anyRequest().denyAll();
                        })
                .build();
    }*/

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf(csrf->csrf.disable())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailServiceImpl userDetailService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailService);
        return provider;
    }
/*    @Bean
    public UserDetailsService userDetailsService() {
        *//*UserDetails userDetails= User.withUsername("jorge")
                .password("1234")
                .roles("ADMIN")
                .authorities("read","create")
                .build();*//*

       List< UserDetails> userDetailsList= new ArrayList<>();
       userDetailsList.add( User.withUsername("jorge")
               .password("1234")
               .roles("ADMIN")
               .authorities("READ","CREATE")
               .build());
        userDetailsList.add( User.withUsername("withney")
                .password("1234")
                .roles("USER")
                .authorities("READ")
                .build());



        return new InMemoryUserDetailsManager(userDetailsList);


    }*/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {

        System.out.println(new BCryptPasswordEncoder().encode("1234"));
    }


}
