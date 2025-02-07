package com.dbmonitor.app.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "sql_jobs")
public class SQLJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private int jobId;

    @Column(name = "job_name")
    private String jobName;

    @Column(name = "status")
    private String status;

    @Column(name = "start_time")
    private Timestamp startTime;

    @Column(name = "end_time")
    private Timestamp endTime;

    @Column(name = "run_duration")
    private Integer runDuration;

    public SQLJob() {}

    public SQLJob(int jobId, String jobName, String status, Timestamp startTime, Timestamp endTime, Integer runDuration) {
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

    public Integer getRunDuration() { return runDuration; }
    public void setRunDuration(Integer runDuration) { this.runDuration = runDuration; }
}
