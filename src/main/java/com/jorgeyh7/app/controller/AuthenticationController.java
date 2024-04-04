package com.jorgeyh7.app.controller;

import com.jorgeyh7.app.controller.dto.AuthLoginRequest;
import com.jorgeyh7.app.controller.dto.AuthResponse;
import com.jorgeyh7.app.service.UserDetailServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

@Autowired
    private UserDetailServiceImpl userDetailService;
    @PostMapping("/log-in")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest){
        return new ResponseEntity<>(this.userDetailService.loginUser(userRequest),HttpStatus.OK);
    }


}
