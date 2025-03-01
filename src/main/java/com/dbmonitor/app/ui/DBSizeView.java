package com.dbmonitor.app.ui;

import com.dbmonitor.app.model.DBSizeLog;
import com.dbmonitor.app.service.DBSizeLogService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@PageTitle("Database Size Logs")
@Route(value = "db-size-logs", layout = MainLayout.class)
public class DBSizeView extends VerticalLayout {

    private final Grid<DBSizeLog> dbGrid = new Grid<>();
    private final DBSizeLogService dbSizeLogService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy; hh:mm a");

    private final TextField databaseSearch = new TextField("Search by Database");

    @Autowired
    public DBSizeView(DBSizeLogService dbSizeLogService) {
        this.dbSizeLogService = dbSizeLogService;
        configureFilters();
        configureGrid();
        loadLogs();

        add(new HorizontalLayout(databaseSearch), dbGrid);
        setSizeFull();
        dbGrid.setSizeFull();
    }

    private void configureFilters() {
        // Search by Database Name
        databaseSearch.setPlaceholder("Enter DB name");
        databaseSearch.setValueChangeMode(ValueChangeMode.EAGER);
        databaseSearch.addValueChangeListener(e -> applyFilters());
        databaseSearch.setPrefixComponent(VaadinIcon.SEARCH.create());
    }

    private void applyFilters() {
        List<DBSizeLog> filteredLogs = dbSizeLogService.getAllLogs().stream()
                .filter(log -> databaseSearch.getValue().isEmpty() ||
                        log.getDatabaseName().toLowerCase().contains(databaseSearch.getValue().trim().toLowerCase()))
                .collect(Collectors.toList());

        dbGrid.setItems(filteredLogs);
    }

    private void configureGrid() {
        dbGrid.addColumn(DBSizeLog::getLogId)
                .setHeader("Log ID")
                .setWidth("100px")
                .setFlexGrow(0);

        dbGrid.addColumn(log -> log.getLogDate() != null
                        ? log.getLogDate().toLocalDateTime().format(DATE_FORMATTER) : "N/A")
                .setHeader("Log Date")
                .setResizable(true)
                .setSortable(true);

        dbGrid.addColumn(DBSizeLog::getDatabaseName)
                .setHeader("Database Name")
                .setResizable(true);

        dbGrid.addColumn(DBSizeLog::getSizeInMB)
                .setHeader("Size (MB)")
                .setResizable(true)
                .setSortable(true);

        dbGrid.addColumn(DBSizeLog::getFileType)
                .setHeader("File Type")
                .setResizable(true);
    }

    @PostConstruct
    private void loadLogs() {
        List<DBSizeLog> logs = dbSizeLogService.getAllLogs();
        dbGrid.setItems(logs);
    }
}
