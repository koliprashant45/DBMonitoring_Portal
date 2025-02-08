package com.dbmonitor.app.controller;

import com.dbmonitor.app.model.LoggedInUser;
import com.dbmonitor.app.service.LoggedInUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loggedinusers")
@CrossOrigin(origins = "*")

public class LoggedInUserController {

    @Autowired
    private LoggedInUserService loggedInUserService;

    @GetMapping
    public List<LoggedInUser> getAllUsers() {
        return loggedInUserService.getAllUsers();
    }
}
