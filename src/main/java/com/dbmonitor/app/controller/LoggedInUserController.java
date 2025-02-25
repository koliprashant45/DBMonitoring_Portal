package com.dbmonitor.app.controller;

import com.dbmonitor.app.model.LoggedInUser;
import com.dbmonitor.app.service.LoggedInUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/logged-in-users")
@CrossOrigin(origins = "*")
public class LoggedInUserController {

    private final LoggedInUserService userService;

    @Autowired
    public LoggedInUserController(LoggedInUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<LoggedInUser> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public LoggedInUser getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @GetMapping("/status/{status}")
    public List<LoggedInUser> getUsersByStatus(@PathVariable String status) {
        return userService.getUsersByStatus(status);
    }

    @GetMapping("/login-time")
    public List<LoggedInUser> getUsersByLoginTime(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return userService.getUsersByLoginTime(start, end);
    }

    @PostMapping
    public LoggedInUser saveUser(@RequestBody LoggedInUser user) {
        return userService.saveUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
    }
}
