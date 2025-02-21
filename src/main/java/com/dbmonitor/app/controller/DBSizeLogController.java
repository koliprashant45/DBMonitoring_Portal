package com.dbmonitor.app.controller;

import com.dbmonitor.app.model.DBSizeLog;
import com.dbmonitor.app.service.DBSizeLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/dbsizelog")
@CrossOrigin(origins = "*")
public class DBSizeLogController {

    @Autowired
    private DBSizeLogService dbSizeLogService;

    @GetMapping
    public List<DBSizeLog> getAllLogs() {
        return dbSizeLogService.getAllLogs();
    }
}
