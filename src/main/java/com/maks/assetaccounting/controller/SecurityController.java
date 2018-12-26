package com.maks.assetaccounting.controller;

import com.maks.assetaccounting.dto.UserDto;
import com.maks.assetaccounting.entity.User;
import com.maks.assetaccounting.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class SecurityController {
    private final UserService userService;

    @Autowired
    public SecurityController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    public UserDto addUser(@RequestBody final UserDto userDto) {
        log.info("registration {}", userDto);
        final User userFromDb = userService.getByName(userDto.getUsername());
        if (userFromDb != null) {
            throw new IllegalArgumentException(userService.get(userFromDb.getId()) + " already exists");
        }
        return userService.create(userDto);
    }
}
