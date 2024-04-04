package com.jorgeyh7.app.service;

import com.jorgeyh7.app.controller.dto.AuthLoginRequest;
import com.jorgeyh7.app.controller.dto.AuthResponse;
import com.jorgeyh7.app.persistence.entity.UserEntity;
import com.jorgeyh7.app.persistence.repository.UserRepository;
import com.jorgeyh7.app.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       UserEntity userEntity=userRepository.findUserEntityByUsername(username)
               .orElseThrow(()->new UsernameNotFoundException("el usuario"+username+"no existe"));

        //creamos una lista de SimpleGrandAuthority de la interface GrantedAuthority, para manejar permisos consedidos

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        //agregamos los roles al authorityList
        userEntity.getRoles()
                .forEach(role->authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));
        //agregamos los permisos al authorityLis
        userEntity.getRoles().stream()
                .flatMap(role-> role.getPermissionList().stream())
                .forEach(permission->authorityList.add(new SimpleGrantedAuthority(permission.getName())));
    //con esto spring security busca los usuarios en la base de datos con los authoritys, en el lenguaje que entiende spring security

        return new User(userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.isEnabled(),
                userEntity.isAccountNoExpired(),
                userEntity.isCredentialNoExpired(),
                userEntity.isAccountNoLocked(),
                authorityList);
    }


    public AuthResponse loginUser(AuthLoginRequest authLoginRequest){
        String username= authLoginRequest.username();
        String password= authLoginRequest.password();

        Authentication authentication=this.authenticate(username,password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accesToken = jwtUtils.createToken(authentication);

        AuthResponse authResponse= new AuthResponse(username,"user loged succesful",accesToken,true);

        return authResponse;

    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails=this.loadUserByUsername(username);

        if(userDetails==null){
            throw  new BadCredentialsException("Invalid user or password");

        }
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw  new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(username,userDetails.getPassword(),userDetails.getAuthorities());


    }
}
