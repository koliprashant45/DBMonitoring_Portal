package com.dbmonitor.app.model;

import java.sql.Timestamp;

public class SQLJob {
    private int jobId;
    private String jobName;
    private String status;
    private Timestamp startTime;
    private Timestamp endTime;
    private int runDuration; // in seconds

    public SQLJob() {}

    public SQLJob(int jobId, String jobName, String status, Timestamp startTime, Timestamp endTime, int runDuration) {
        this.jobId = jobId;
        this.jobName = jobName;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.runDuration = runDuration;
    }

    // Getters and Setters
    public int getJobId() { return jobId; }
    public void setJobId(int jobId) { this.jobId = jobId; }

    public String getJobName() { return jobName; }
    public void setJobName(String jobName) { this.jobName = jobName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getStartTime() { return startTime; }
    public void setStartTime(Timestamp startTime) { this.startTime = startTime; }

    public Timestamp getEndTime() { return endTime; }
    public void setEndTime(Timestamp endTime) { this.endTime = endTime; }

    public int getRunDuration() { return runDuration; }
    public void setRunDuration(int runDuration) { this.runDuration = runDuration; }
}
