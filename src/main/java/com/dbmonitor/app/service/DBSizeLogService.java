package com.dbmonitor.app.service;

import com.dbmonitor.app.model.DBSizeLog;
import com.dbmonitor.app.repository.DBSizeLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DBSizeLogService {

    @Autowired
    private DBSizeLogRepository dbSizeLogRepository;

    public List<DBSizeLog> getAllLogs() {

        return dbSizeLogRepository.findAll();
    }
}
