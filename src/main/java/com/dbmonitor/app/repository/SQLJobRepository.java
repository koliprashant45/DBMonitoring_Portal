package com.dbmonitor.app.repository;

import com.dbmonitor.app.model.SQLJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SQLJobRepository extends JpaRepository<SQLJob, Integer> {
}
