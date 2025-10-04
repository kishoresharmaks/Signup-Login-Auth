package com.nexus.authentication.controller;

import com.nexus.authentication.model.User;
import com.nexus.authentication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService service;
    @GetMapping
    public List<User> getAllUsers(){
        return service.getAllUser();
    }
    @PostMapping("/create")
    public User register(@RequestBody User u){
        return service.register(u);
    }

    @PutMapping("/update/{id}")
    public User updateUser(@PathVariable Long id,@RequestBody User updateuser){
        return service.updateUser(id,updateuser);
    }
    @DeleteMapping("/remove/{id}")
    public String deleteUser(@PathVariable Long id) {
        return service.deleteUser(id);
    }

}
