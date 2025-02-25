package com.dbmonitor.app.service;

import com.dbmonitor.app.model.LoggedInUser;
import com.dbmonitor.app.repository.LoggedInUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoggedInUserService {

    private final LoggedInUserRepository userRepository;

    @Autowired
    public LoggedInUserService(LoggedInUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<LoggedInUser> getAllUsers() {
        return userRepository.findAll();
    }

    public LoggedInUser getUserById(int userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public List<LoggedInUser> getUsersByStatus(String status) {
        return userRepository.findByStatus(status);
    }

    public List<LoggedInUser> getUsersByLoginTime(LocalDateTime start, LocalDateTime end) {
        return userRepository.findByLoginTimeBetween(start, end);
    }

    public LoggedInUser saveUser(LoggedInUser user) {
        return userRepository.save(user);
    }

    public void deleteUser(int userId) {
        userRepository.deleteById(userId);
    }

    public void terminateUser(int userId) {
        LoggedInUser user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setStatus("Logged Out");
            user.setLogoutTime(Timestamp.from(Instant.now()));
            userRepository.save(user);
        }
    }
}
