package com.dbmonitor.app.dao;

import com.dbmonitor.app.config.DatabaseConfig;
import com.dbmonitor.app.model.DBSizeLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBSizeDao {

    public List<DBSizeLog> getDatabaseSizeLogs() {
        List<DBSizeLog> logs = new ArrayList<>();
        String query = "SELECT * FROM db_size_log ORDER BY log_date DESC";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                logs.add(new DBSizeLog(
                        rs.getInt("log_id"),
                        rs.getTimestamp("log_date"),
                        rs.getString("database_name"),
                        rs.getDouble("size_in_mb")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }
}