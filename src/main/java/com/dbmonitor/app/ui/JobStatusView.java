package com.dbmonitor.app.ui;

import com.dbmonitor.app.model.SQLJob;
import com.dbmonitor.app.service.SQLJobService;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@PageTitle("SQL Job Status")
@Route(value = "job-status", layout = MainLayout.class)
public class JobStatusView extends VerticalLayout {

    private final Grid<SQLJob> jobGrid = new Grid<>();
    private final SQLJobService jobService;
    private final Select<String> statusFilter = new Select<>();
    private final DatePicker dateFilter = new DatePicker("Filter by Date");

    @Autowired
    public JobStatusView(SQLJobService jobService) {
        this.jobService = jobService;
        configureFilters();
        configureGrid();
        add(new HorizontalLayout(statusFilter, dateFilter), jobGrid);

        setSizeFull();
        jobGrid.setSizeFull();
    }

    private void configureFilters() {
        // Status Filter
        statusFilter.setLabel("Filter by Status");
        statusFilter.setItems("All", "Completed", "Running", "Success", "Failed");
        statusFilter.setValue("All");
        statusFilter.addValueChangeListener(e -> applyFilters());

        // Date Filter
        dateFilter.addValueChangeListener(e -> applyFilters());
    }

    private void applyFilters() {
        List<SQLJob> filteredJobs = jobService.getAllJobs().stream()
                .filter(job -> "All".equals(statusFilter.getValue()) || job.getStatus().equals(statusFilter.getValue()))
                .filter(job -> dateFilter.getValue() == null ||
                        (job.getStartTime() != null && job.getStartTime().toLocalDateTime().toLocalDate().equals(dateFilter.getValue())))
                .collect(Collectors.toList());

        jobGrid.setItems(filteredJobs);
    }

    private void configureGrid() {
        jobGrid.addColumn(SQLJob::getJobId).setHeader("Job ID").setWidth("80px").setFlexGrow(0);
        jobGrid.addColumn(SQLJob::getJobName).setHeader("Job Name").setResizable(true);
        jobGrid.addColumn(SQLJob::getStatus).setHeader("Status").setResizable(true);
        jobGrid.addColumn(job -> job.getStartTime() != null ? job.getStartTime().toString() : "N/A")
                .setHeader("Start Time").setResizable(true);
        jobGrid.addColumn(job -> job.getEndTime() != null ? job.getEndTime().toString() : "N/A")
                .setHeader("End Time").setResizable(true);
        jobGrid.addColumn(SQLJob::getRunDuration).setHeader("Run Duration").setResizable(true);

    }

    @PostConstruct
    private void loadJobs() {
        jobGrid.setItems(jobService.getAllJobs());
    }
}
