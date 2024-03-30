package dev.sbohdan.h2.controller;

import dev.sbohdan.h2.dto.UserDto;
import dev.sbohdan.h2.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public void addUser(@RequestBody UserDto newUser) {
        userService.addUer(newUser);
    }
}
