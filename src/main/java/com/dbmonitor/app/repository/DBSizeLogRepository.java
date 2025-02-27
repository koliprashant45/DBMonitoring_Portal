package com.dbmonitor.app.repository;

import com.dbmonitor.app.model.DBSizeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DBSizeLogRepository extends JpaRepository<DBSizeLog, Integer> {

    List<DBSizeLog> findByDatabaseNameContainingIgnoreCase(String databaseName);
}
