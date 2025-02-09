package com.dbmonitor.app.repository;

import com.dbmonitor.app.model.LoggedInUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoggedInUserRepository extends JpaRepository<LoggedInUser, Integer> {
}
