package com.jorgeyh7.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@PreAuthorize("denyAll()")
public class TestController {
    @GetMapping("/get")
    @PreAuthorize("hasAuthority('READ')")
    public String helloGet() {
        return "welcome - GET";
    }

    @PostMapping("/post")
    @PreAuthorize("hasAuthority('CREATE')"  )
    public String helloPost() {
        return "welcome - POST";
    }

    @PutMapping("/put")
    public String helloPut() {
        return "welcome - PUT";
    }

    @DeleteMapping("/delete")
    public String helloDelete() {
        return "welcome - DELETE";
    }

    @PatchMapping("/patch")
    @PreAuthorize("hasAuthority('REFACTOR')")
    public String helloPatch() {
        return "welcome - PATCH";
    }



}
