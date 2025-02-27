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

    @Column(name = "database_name", nullable = false)
    private String databaseName;

    @Column(name = "size_in_mb", nullable = false)
    private double sizeInMB;

    @Column(name = "log_date", nullable = false)
    private Timestamp logDate;

    @Column(name = "file_type", nullable = false)
    private String fileType;

    public DBSizeLog() {}

    public DBSizeLog(int logId, String databaseName, double sizeInMB, Timestamp logDate, String fileType) {
        this.logId = logId;
        this.databaseName = databaseName;
        this.sizeInMB = sizeInMB;
        this.logDate = logDate;
        this.fileType = fileType;
    }

    public int getLogId() { return logId; }
    public void setLogId(int logId) { this.logId = logId; }

    public String getDatabaseName() { return databaseName; }
    public void setDatabaseName(String databaseName) { this.databaseName = databaseName; }

    public double getSizeInMB() { return sizeInMB; }
    public void setSizeInMB(double sizeInMB) { this.sizeInMB = sizeInMB; }

    public Timestamp getLogDate() { return logDate; }
    public void setLogDate(Timestamp logDate) { this.logDate = logDate; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
}
