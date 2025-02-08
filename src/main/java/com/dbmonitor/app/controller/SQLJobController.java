package com.dbmonitor.app.controller;

import com.dbmonitor.app.model.SQLJob;
import com.dbmonitor.app.service.SQLJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sqljobs")
@CrossOrigin(origins = "*")
public class SQLJobController {

    @Autowired
    private SQLJobService sqlJobService;

    @GetMapping
    public List<SQLJob> getAllJobs() {
        return sqlJobService.getAllJobs();
    }
}

