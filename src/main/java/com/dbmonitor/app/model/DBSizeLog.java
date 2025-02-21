package com.dbmonitor.app.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "db_size_log")
public class DBSizeLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "log_id")
    private int logId;

    @Column(name = "database_name")
    private String databaseName;

    @Column(name = "size_in_mb")
    private double sizeInMB;

    @Column(name = "log_date")
    private Timestamp logDate;

    public DBSizeLog() {}

    public DBSizeLog(int logId, String databaseName, double sizeInMB, Timestamp logDate) {
        this.logId = logId;
        this.databaseName = databaseName;
        this.sizeInMB = sizeInMB;
        this.logDate = logDate;
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
