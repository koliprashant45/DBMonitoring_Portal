package com.dbmonitor.app.ui;

import com.dbmonitor.app.model.SQLJob;
import com.dbmonitor.app.service.SQLJobService;
import com.dbmonitor.app.util.DateRangePicker;
import com.dbmonitor.app.util.LocalDateRange;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.PostConstruct;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@PageTitle("Job Status")
@Route(value = "job-status", layout = MainLayout.class)
public class JobStatusView extends VerticalLayout {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy; hh:mm a");

    private final Grid<SQLJob> jobGrid;
    private final SQLJobService jobService;
    private final Select<String> statusFilter;
    private final DateRangePicker dateRangeFilter;
    private final Button exportButton;

    @Autowired
    public JobStatusView(SQLJobService jobService) {
        this.jobService = Objects.requireNonNull(jobService, "SQLJobService must not be null");
        this.jobGrid = new Grid<>(SQLJob.class, false);
        this.statusFilter = new Select<>();
        this.dateRangeFilter = new DateRangePicker("Filter by Date Range");
        this.exportButton = new Button("Export to Excel", VaadinIcon.DOWNLOAD.create(), e -> showExportDialog());

        configureComponents();
        buildLayout();
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
        jobGrid.addColumn(SQLJob::getRunDuration).setHeader("Run Duration (min)").setResizable(true).setSortable(true);
    }

    private void configureFilters() {
        statusFilter.setLabel("Filter by Status");
        statusFilter.setItems("All", "Completed", "Running", "Pending", "Failed");
        statusFilter.setValue("All");
        statusFilter.addValueChangeListener(e -> refreshGrid());

        dateRangeFilter.addValueChangeListener(e -> refreshGrid());
    }

    private void buildLayout() {
        HorizontalLayout filterLayout = new HorizontalLayout(statusFilter, dateRangeFilter, exportButton);
        filterLayout.setSpacing(true);
        filterLayout.setAlignItems(Alignment.BASELINE);

        HorizontalLayout mainLayout = new HorizontalLayout(filterLayout, exportButton);
        mainLayout.setWidthFull();
        mainLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        mainLayout.setAlignItems(Alignment.BASELINE);

        add(mainLayout, jobGrid);
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

    private void showExportDialog() {

        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Export Data");

        // Generate Excel File
        StreamResource excelFile = generateExcelFile();
        if (excelFile == null) {
            Notification.show("No data available for export!", 3000, Notification.Position.MIDDLE);
            return;
        }

        // Excel Icon
        Icon excelIcon = VaadinIcon.FILE_TABLE.create();
        excelIcon.setSize("22px");
        excelIcon.getStyle().set("color", "#007BFF");

        // Download Link
        Anchor downloadLink = new Anchor(excelFile, "Download SQL Job Data");
        downloadLink.getElement().setAttribute("download", "SQL_Job_Status.xlsx");
        downloadLink.getStyle()
                .set("font-size", "16px")
                .set("font-weight", "bold")
                .set("color", "#007BFF")
                .set("text-decoration", "none");

        downloadLink.getElement().addEventListener("click", e ->
                Notification.show("Excel file downloaded successfully!", 3000, Notification.Position.BOTTOM_START)
        );

        HorizontalLayout downloadLayout = new HorizontalLayout(excelIcon, downloadLink);
        downloadLayout.setAlignItems(Alignment.CENTER);

        dialog.setText(downloadLayout);

        dialog.setConfirmText("Close");
        dialog.addConfirmListener(event -> dialog.close());

        dialog.open();
    }

    private StreamResource generateExcelFile() {
        List<SQLJob> jobs = jobGrid.getListDataView().getItems().toList();

        if (jobs.isEmpty()) {
            Notification.show("No data to export!", 3000, Notification.Position.MIDDLE);
            return null;
        }

        return new StreamResource("SQL_Job_Status.xlsx", () -> {
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("SQL Jobs");

                // header row
                Row headerRow = sheet.createRow(0);
                String[] headers = {"Job ID", "Job Name", "Status", "Start Time", "End Time", "Run Duration (min)"};
                for (int i = 0; i < headers.length; i++) {
                    headerRow.createCell(i).setCellValue(headers[i]);
                }

                // job data
                int rowNum = 1;
                for (SQLJob job : jobs) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(job.getJobId());
                    row.createCell(1).setCellValue(job.getJobName() != null ? job.getJobName() : "N/A");
                    row.createCell(2).setCellValue(job.getStatus() != null ? job.getStatus() : "N/A");
                    row.createCell(3).setCellValue(formatStartTime(job));
                    row.createCell(4).setCellValue(formatEndTime(job));
                    row.createCell(5).setCellValue(job.getRunDuration() != null ? job.getRunDuration() : 0);
                }

                // Auto-size columns
                for (int i = 0; i < headers.length; i++) {
                    sheet.autoSizeColumn(i);
                }

                // Write data to stream
                workbook.write(out);
                workbook.close();
                return new ByteArrayInputStream(out.toByteArray());
            } catch (IOException e) {
                throw new RuntimeException("Error generating Excel file", e);
            }
        });
    }
}



