package com.dbmonitor.app.service;

import com.dbmonitor.app.model.LoggedInUser;
import com.dbmonitor.app.repository.LoggedInUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoggedInUserService {

    @Autowired
    private LoggedInUserRepository loggedInUserRepository;

    public List<LoggedInUser> getAllUsers() {
        return loggedInUserRepository.findAll();
    }
}
