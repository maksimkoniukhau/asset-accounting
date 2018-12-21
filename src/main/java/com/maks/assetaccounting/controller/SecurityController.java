package com.maks.assetaccounting.controller;

import com.maks.assetaccounting.model.Role;
import com.maks.assetaccounting.model.User;
import com.maks.assetaccounting.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@Slf4j
public class SecurityController {
    private final UserService userService;

    @Autowired
    public SecurityController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public User addUser(@RequestBody User user) {
        User userFromDb = userService.getByName(user.getUsername());
        if (userFromDb != null) {
            throw new IllegalArgumentException(user + " already exists");
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        return userService.create(user);
    }
}
