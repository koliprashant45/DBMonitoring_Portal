package com.dbmonitor.app.repository;

import com.dbmonitor.app.model.LoggedInUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LoggedInUserRepository extends JpaRepository<LoggedInUser, Integer> {
    List<LoggedInUser> findByStatus(String status);
    List<LoggedInUser> findByLoginTimeBetween(LocalDateTime start, LocalDateTime end);
}
