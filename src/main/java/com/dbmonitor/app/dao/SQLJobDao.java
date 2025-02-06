package com.dbmonitor.app.dao;

import com.dbmonitor.app.config.DatabaseConfig;
import com.dbmonitor.app.model.SQLJob;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLJobDao {

    public List<SQLJob> getAllJobs() {
        List<SQLJob> jobs = new ArrayList<>();
        String query = "SELECT * FROM sql_jobs";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                jobs.add(new SQLJob(
                        rs.getInt("job_id"),
                        rs.getString("job_name"),
                        rs.getString("status"),
                        rs.getTimestamp("start_time"),
                        rs.getTimestamp("end_time"),
                        rs.getInt("run_duration")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jobs;
    }
}
