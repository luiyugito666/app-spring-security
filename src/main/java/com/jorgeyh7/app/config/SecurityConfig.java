package com.jorgeyh7.app.config;

import com.jorgeyh7.app.config.filter.JwtTokenValidator;
import com.jorgeyh7.app.service.UserDetailServiceImpl;
import com.jorgeyh7.app.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.AuthorizeHttpRequestsDsl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity//configuraciones con anotaciones
public class SecurityConfig {

@Autowired
    private JwtUtils jwtUtils;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf(csrf->csrf.disable())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http->{
                        //configure endpoints publics
                        http.requestMatchers(HttpMethod.POST,"auth/**").permitAll();

                        //configure endpoints privates
                        http.requestMatchers(HttpMethod.POST, "/method/post").hasAnyRole("ADMIN","DEVELOPER");
                        http.requestMatchers(HttpMethod.PATCH, "/method/patch").hasAnyAuthority("REFACTOR");
                        http.requestMatchers(HttpMethod.GET, "/method/get").hasAnyRole("INVITED", "ADMIN");
                         //configure other endpoints
                        http.anyRequest().denyAll();
                        })
                .addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class)
                .build();
    }


   // securityFilterChain, trabaja con notaciones para el controles en la parte de AuthorizeHttpRequests, con el @PreAutorize
  /*  @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf(csrf->csrf.disable())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class)
                .build();
    }*/

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




}
