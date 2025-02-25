package com.dbmonitor.app.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "logged_in_users")
public class LoggedInUser {

    @Id
    @Column(name = "user_id")
    private int userId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "login_time", nullable = false)
    private Timestamp loginTime;

    @Column(name = "logout_time")
    private Timestamp logoutTime;

    @Column(name = "user_role")
    private String userRole;

    @Column(name = "status", nullable = false)
    private String status;

    public LoggedInUser() {}

    public LoggedInUser(int userId, String username, Timestamp loginTime, Timestamp logoutTime, String userRole, String status) {
        this.userId = userId;
        this.username = username;
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;
        this.userRole = userRole;
        this.status = status;
    }

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Timestamp getLoginTime() { return loginTime; }
    public void setLoginTime(Timestamp loginTime) { this.loginTime = loginTime; }

    public Timestamp getLogoutTime() { return logoutTime; }
    public void setLogoutTime(Timestamp logoutTime) { this.logoutTime = logoutTime; }

    public String getUserRole() { return userRole; }
    public void setUserRole(String userRole) { this.userRole = userRole; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
