package com.maks.assetaccounting.controller;

import com.maks.assetaccounting.dto.user.UserToBackDto;
import com.maks.assetaccounting.dto.user.UserToFrontDto;
import com.maks.assetaccounting.entity.User;
import com.maks.assetaccounting.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class SecurityController {
    private final UserService userService;

    @Autowired
    public SecurityController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public UserToFrontDto addUser(@RequestBody UserToBackDto userToBackDto) {
        User userFromDb = userService.getByName(userToBackDto.getUsername());
        if (userFromDb != null) {
            throw new IllegalArgumentException(userService.get(userToBackDto.getId()) + " already exists");
        }
        return userService.create(userToBackDto);
    }
}
