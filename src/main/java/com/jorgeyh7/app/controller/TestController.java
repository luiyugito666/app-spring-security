package com.jorgeyh7.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@PreAuthorize("denyAll()")
public class TestController {

    @GetMapping("/hello")
    @PreAuthorize("permitAll()")
    public String hello(){

        return "hello world";
    }
    @GetMapping("/hello-secured")
    @PreAuthorize("hasAuthority('CREATE')")
    public String helloSecured(){

        return "hello world secured";
    }
    @GetMapping("/hello-secured2")
    @PreAuthorize("hasAuthority('READ')")
    public String helloSecured2(){

        return "hello world secured2";
    }



}
