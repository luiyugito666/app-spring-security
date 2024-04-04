package com.jorgeyh7.app.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/method")
public class TestController {
    @GetMapping("/get")
    public String helloGet() {
        return "welcome - GET";
    }

    @PostMapping("/post")
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
      public String helloPatch() {
        return "welcome - PATCH";
    }



}
