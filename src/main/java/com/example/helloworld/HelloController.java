package com.example.helloworld;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController 
@RequestMapping("/api/users")
public class HelloController {
   
   @GetMapping("/hello")
    public String sayHello() {
        return "Hello, World!";
    }

    @GetMapping("/login")
    public String userLogin() {
        return "User Login";
    }

    @GetMapping("/uid/{id}")
    public String getUser(@PathVariable Long id) {
        return "User ID: " + id;
    }

    @GetMapping("/all")
    public String getAllUserIds() {
        return "All User IDs";
    }

}
