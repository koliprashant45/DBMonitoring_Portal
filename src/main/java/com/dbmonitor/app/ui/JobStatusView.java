package com.dbmonitor.app.ui;

import com.dbmonitor.app.model.SQLJob;
import com.dbmonitor.app.service.SQLJobService;
import com.dbmonitor.app.util.DateRangePicker;
import com.dbmonitor.app.util.LocalDateRange;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@PageTitle("SQL Job Status")
@Route(value = "job-status", layout = MainLayout.class)
public class JobStatusView extends VerticalLayout {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy; hh:mm a");

    private final Grid<SQLJob> jobGrid;
    private final SQLJobService jobService;
    private final Select<String> statusFilter;
    private final DateRangePicker dateRangeFilter;

    @Autowired
    public JobStatusView(SQLJobService jobService) {
        this.jobService = Objects.requireNonNull(jobService, "SQLJobService must not be null");
        this.jobGrid = new Grid<>(SQLJob.class, false);
        this.statusFilter = new Select<>();
        this.dateRangeFilter = new DateRangePicker("Filter by Date Range");

        configureComponents();
        buildLayout();
        loadInitialData();
    }

    @PostConstruct
    private void loadInitialData() {
        refreshGrid();
    }

    private void configureComponents() {
        configureGrid();
        configureFilters();
    }

    private void configureGrid() {
        jobGrid.addColumn(SQLJob::getJobId).setHeader("Job ID").setWidth("80px").setFlexGrow(0);
        jobGrid.addColumn(SQLJob::getJobName).setHeader("Job Name").setResizable(true).setSortable(true);
        jobGrid.addColumn(SQLJob::getStatus).setHeader("Status").setResizable(true).setSortable(true);
        jobGrid.addColumn(this::formatStartTime).setHeader("Start Time").setResizable(true).setSortable(true);
        jobGrid.addColumn(this::formatEndTime).setHeader("End Time").setResizable(true).setSortable(true);
        jobGrid.addColumn(SQLJob::getRunDuration).setHeader("Run Duration").setResizable(true).setSortable(true);
    }

    private void configureFilters() {
        statusFilter.setLabel("Filter by Status");
        statusFilter.setItems("All", "Completed", "Running", "Pending", "Failed");
        statusFilter.setValue("All");
        statusFilter.addValueChangeListener(e -> refreshGrid());

        dateRangeFilter.addValueChangeListener(e -> refreshGrid());
    }

    private void buildLayout() {
        HorizontalLayout filterLayout = new HorizontalLayout(statusFilter, dateRangeFilter);
        filterLayout.setSpacing(true);
        filterLayout.setAlignItems(Alignment.BASELINE);

        add(filterLayout, jobGrid);
        setSizeFull();
        jobGrid.setSizeFull();
    }

    private void refreshGrid() {
        LocalDateRange range = dateRangeFilter.getValue();
        LocalDate startDate = range != null ? range.getStartDate() : null;
        LocalDate endDate = range != null ? range.getEndDate() : null;

        List<SQLJob> filteredJobs = jobService.getAllJobs().stream()
                .filter(job -> filterByStatus(job.getStatus()))
                .filter(job -> filterByDateRange(job, startDate, endDate))
                .toList();

        jobGrid.setItems(filteredJobs);
    }

    private boolean filterByStatus(String jobStatus) {
        String filterValue = statusFilter.getValue();
        return "All".equals(filterValue) || Objects.equals(filterValue, jobStatus);
    }

    private boolean filterByDateRange(SQLJob job, LocalDate startDate, LocalDate endDate) {
        if (job.getStartTime() == null) return false;
        LocalDate jobDate = job.getStartTime().toLocalDateTime().toLocalDate();
        return (startDate == null && endDate == null) ||
                (startDate != null && endDate != null && !jobDate.isBefore(startDate) && !jobDate.isAfter(endDate)) ||
                (startDate != null && endDate == null && !jobDate.isBefore(startDate)) ||
                (endDate != null && startDate == null && !jobDate.isAfter(endDate));
    }

    private String formatStartTime(SQLJob job) {
        return job.getStartTime() != null ? job.getStartTime().toLocalDateTime().format(DATE_FORMATTER) : "N/A";
    }

    private String formatEndTime(SQLJob job) {
        return job.getEndTime() != null ? job.getEndTime().toLocalDateTime().format(DATE_FORMATTER) : "N/A";
    }
}
