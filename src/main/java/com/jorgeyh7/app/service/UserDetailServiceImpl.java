package com.jorgeyh7.app.service;

import com.jorgeyh7.app.persistence.entity.UserEntity;
import com.jorgeyh7.app.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
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
}
