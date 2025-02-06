package com.dbmonitor.app.dao;

import com.dbmonitor.app.config.DatabaseConfig;
import com.dbmonitor.app.model.LoggedInUser;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoggedInUserDao {

    public List<LoggedInUser> getLoggedInUsers() {
        List<LoggedInUser> users = new ArrayList<>();
        String query = "SELECT * FROM logged_in_users";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(new LoggedInUser(
                        rs.getInt("session_id"),
                        rs.getString("username"),
                        rs.getTimestamp("login_time")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}