package com.dbmonitor.app.service;

import com.dbmonitor.app.model.SQLJob;
import com.dbmonitor.app.repository.SQLJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SQLJobService {

    @Autowired
    private SQLJobRepository sqlJobRepository;

    public List<SQLJob> getAllJobs() {
        return sqlJobRepository.findAll();
    }
}
