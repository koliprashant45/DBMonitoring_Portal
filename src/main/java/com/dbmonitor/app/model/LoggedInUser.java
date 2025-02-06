package com.dbmonitor.app.model;

import java.sql.Timestamp;

public class LoggedInUser {
    private String sessionId;
    private String username;
    private String hostName;
    private String databaseName;
    private Timestamp loginTime;

    public LoggedInUser() {}

    public LoggedInUser(String sessionId, String username, String hostName, String databaseName, Timestamp loginTime) {
        this.sessionId = sessionId;
        this.username = username;
        this.hostName = hostName;
        this.databaseName = databaseName;
        this.loginTime = loginTime;
    }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getHostName() { return hostName; }
    public void setHostName(String hostName) { this.hostName = hostName; }

    public String getDatabaseName() { return databaseName; }
    public void setDatabaseName(String databaseName) { this.databaseName = databaseName; }

    public Timestamp getLoginTime() { return loginTime; }
    public void setLoginTime(Timestamp loginTime) { this.loginTime = loginTime; }
}
