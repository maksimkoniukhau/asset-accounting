package com.maks.assetaccounting.controller;

import com.maks.assetaccounting.dto.UserDto;
import com.maks.assetaccounting.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto createUser(@RequestBody final UserDto userDto) {
        log.info("create {}", userDto);
        return userService.create(userDto);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("get all users");
        return userService.getAll();
    }

    @GetMapping("{id}")
    public UserDto getUser(@PathVariable("id") final Long id) {
        log.info("get user with id = {}", id);
        return userService.get(id);
    }
}
