package com.dbmonitor.app.model;

import java.sql.Timestamp;

public class DBSizeLog {
    private int logId;
    private Timestamp logDate;
    private String databaseName;
    private double sizeInMB;

    public DBSizeLog() {}

    public DBSizeLog(int logId, Timestamp logDate, String databaseName, double sizeInMB) {
        this.logId = logId;
        this.logDate = logDate;
        this.databaseName = databaseName;
        this.sizeInMB = sizeInMB;
    }

    // Getters and Setters
    public int getLogId() { return logId; }
    public void setLogId(int logId) { this.logId = logId; }

    public Timestamp getLogDate() { return logDate; }
    public void setLogDate(Timestamp logDate) { this.logDate = logDate; }

    public String getDatabaseName() { return databaseName; }
    public void setDatabaseName(String databaseName) { this.databaseName = databaseName; }

    public double getSizeInMB() { return sizeInMB; }
    public void setSizeInMB(double sizeInMB) { this.sizeInMB = sizeInMB; }
}
