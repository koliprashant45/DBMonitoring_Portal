package com.dbmonitor.app.service;

import com.dbmonitor.app.model.LoggedInUser;
import com.dbmonitor.app.model.SQLJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final SQLJobService sqlJobService;
    private final DBSizeLogService dbSizeLogService;
    private final LoggedInUserService loggedInUserService;

    @Autowired
    public DashboardService(SQLJobService sqlJobService, DBSizeLogService dbSizeLogService, LoggedInUserService loggedInUserService) {
        this.sqlJobService = sqlJobService;
        this.dbSizeLogService = dbSizeLogService;
        this.loggedInUserService = loggedInUserService;
    }

    public Map<String, Long> getJobStatusCounts() {
        List<SQLJob> jobs = sqlJobService.getAllJobs();
        return jobs.stream()
                .collect(Collectors.groupingBy(SQLJob::getStatus, Collectors.counting()));
    }

    public Map<String, Long> getLoggedInUserCounts() {
        List<LoggedInUser> users = loggedInUserService.getAllUsers();
        return users.stream()
                .collect(Collectors.groupingBy(LoggedInUser::getStatus, Collectors.counting())); // Returns Map<String, Long>
    }

    public String getMaxDatabaseSize() {
        return dbSizeLogService.getAllLogs().stream()
                .filter(log -> !List.of("master", "model", "tempdb", "msdb").contains(log.getDatabaseName().toLowerCase()))
                .max((a, b) -> Double.compare(a.getSizeInMB(), b.getSizeInMB())) // Find max
                .map(log -> log.getDatabaseName() + ": " + log.getSizeInMB() + " MB")
                .orElse("No database size available");
    }
}
