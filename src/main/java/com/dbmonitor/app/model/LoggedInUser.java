package com.dbmonitor.app.model;

import java.sql.Timestamp;

public class LoggedInUser {
    private int sessionId;
    private String username;
    private Timestamp loginTime;

    public LoggedInUser() {}

    public LoggedInUser(int sessionId, String username, Timestamp loginTime) {
        this.sessionId = sessionId;
        this.username = username;
        this.loginTime = loginTime;
    }

    // Getters and Setters
    public int getSessionId() { return sessionId; }
    public void setSessionId(int sessionId) { this.sessionId = sessionId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Timestamp getLoginTime() { return loginTime; }
    public void setLoginTime(Timestamp loginTime) { this.loginTime = loginTime; }
}
