package com.dbmonitor.app.ui;

import com.dbmonitor.app.model.LoggedInUser;
import com.dbmonitor.app.service.LoggedInUserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.PostConstruct;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@PageTitle("Logged-in Users")
@Route(value = "logged-in-users", layout = MainLayout.class)
public class LoggedInUsersView extends VerticalLayout {

    private final Grid<LoggedInUser> userGrid = new Grid<>();
    private final LoggedInUserService userService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy; hh:mm a");
    private final Select<String> statusFilter = new Select<>();
    private final Select<String> roleFilter = new Select<>();
    private final TextField usernameSearch = new TextField("Search by Username");

    @Autowired
    public LoggedInUsersView(LoggedInUserService userService) {
        this.userService = userService;
        configureFilters();
        configureGrid();

        Button exportButton = new Button("Export to Excel",VaadinIcon.DOWNLOAD.create(), event -> showExportDialog());
        exportButton.getStyle().set("margin-left", "auto");

        HorizontalLayout topLayout = new HorizontalLayout(roleFilter, statusFilter, usernameSearch, exportButton);
        topLayout.setWidthFull();
        topLayout.setAlignItems(Alignment.END);

        add(topLayout, userGrid);
        setSizeFull();
        userGrid.setSizeFull();
    }

    private void configureFilters() {
        roleFilter.setLabel("Filter by Role");
        roleFilter.setItems("All", "Admin", "User", "Developer", "Support Engineer");
        roleFilter.setValue("All");
        roleFilter.addValueChangeListener(e -> applyFilters());

        statusFilter.setLabel("Filter by Status");
        statusFilter.setItems("All", "Active", "Inactive", "Logged Out");
        statusFilter.setValue("All");
        statusFilter.addValueChangeListener(e -> applyFilters());

        usernameSearch.setPlaceholder("Enter username");
        usernameSearch.setValueChangeMode(ValueChangeMode.EAGER);
        usernameSearch.addValueChangeListener(e -> applyFilters());
        usernameSearch.setPrefixComponent(VaadinIcon.SEARCH.create());
    }

    private void applyFilters() {
        List<LoggedInUser> filteredUsers = userService.getAllUsers().stream()
                .filter(user -> "All".equals(statusFilter.getValue()) || user.getStatus().equalsIgnoreCase(statusFilter.getValue()))
                .filter(user -> "All".equals(roleFilter.getValue()) || user.getUserRole().equalsIgnoreCase(roleFilter.getValue()))
                .filter(user -> usernameSearch.getValue().isEmpty() ||
                        user.getUsername().toLowerCase().contains(usernameSearch.getValue().trim().toLowerCase()))
                .collect(Collectors.toList());

        userGrid.setItems(filteredUsers);
    }

    private void configureGrid() {
        userGrid.addColumn(LoggedInUser::getUserId).setHeader("User ID").setWidth("80px").setFlexGrow(0);
        userGrid.addColumn(LoggedInUser::getUsername).setHeader("Username").setResizable(true);
        userGrid.addColumn(LoggedInUser::getUserRole).setHeader("User Role").setResizable(true).setSortable(true);
        userGrid.addColumn(user -> user.getLoginTime() != null
                        ? user.getLoginTime().toLocalDateTime().format(DATE_FORMATTER) : "N/A")
                .setHeader("Login Time").setResizable(true).setSortable(true);
        userGrid.addColumn(user -> user.getLogoutTime() != null
                        ? user.getLogoutTime().toLocalDateTime().format(DATE_FORMATTER) : "N/A")
                .setHeader("Logout Time").setResizable(true).setSortable(true);
        userGrid.addColumn(LoggedInUser::getStatus).setHeader("Status").setResizable(true).setSortable(true);

        userGrid.addComponentColumn(user -> {
            Button terminateButton = new Button("Terminate", event -> showConfirmationDialog(user));
            terminateButton.setTooltipText("Terminate user");
            terminateButton.getStyle().set("background-color", "#ff3333").set("color", "white");

            boolean isActive = "active".equalsIgnoreCase(user.getStatus());
            terminateButton.setEnabled(isActive);
            if (!isActive) {
                terminateButton.getStyle().set("background-color", "#cccccc").set("color", "#666666");
            }
            return terminateButton;
        }).setHeader("Actions");
    }

    private void showConfirmationDialog(LoggedInUser user) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Confirm Termination");
        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.add("Are you sure you want to terminate session for " + user.getUsername() + "?");
        dialog.add(dialogLayout);

        Button confirmButton = new Button("Yes", event -> {
            terminateUser(user);
            dialog.close();
        });
        confirmButton.getStyle().set("background-color", "#0000ff").set("color", "white");

        Button cancelButton = new Button("No", event -> dialog.close());
        cancelButton.getStyle().set("background-color", "#808080").set("color", "white");

        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(confirmButton);
        dialog.open();
    }

    private void terminateUser(LoggedInUser user) {
        userService.terminateUser(user.getUserId());
        Notification.show("User " + user.getUsername() + " has been terminated.");
        loadUsers();
    }

    @PostConstruct
    private void loadUsers() {
        List<LoggedInUser> users = userService.getAllUsers();
        userGrid.setItems(users);
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
        Anchor downloadLink = new Anchor(excelFile, "Download Logged-In Users Data");
        downloadLink.getElement().setAttribute("download", "LoggedIn_Users.xlsx");
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
        List<LoggedInUser> users = userGrid.getListDataView().getItems().toList();

        if (users.isEmpty()) {
            Notification.show("No data to export!", 3000, Notification.Position.MIDDLE);
            return null;
        }

        return new StreamResource("LoggedIn_Users.xlsx", () -> {
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Logged-in Users");

                // Header Row
                Row headerRow = sheet.createRow(0);
                String[] headers = {"User ID", "Username", "User Role", "Login Time", "Logout Time", "Status"};
                for (int i = 0; i < headers.length; i++) {
                    headerRow.createCell(i).setCellValue(headers[i]);
                }

                // Data Rows
                int rowIdx = 1;
                for (LoggedInUser user : users) {
                    Row row = sheet.createRow(rowIdx++);
                    row.createCell(0).setCellValue(user.getUserId());
                    row.createCell(1).setCellValue(user.getUsername() != null ? user.getUsername() : "N/A");
                    row.createCell(2).setCellValue(user.getUserRole() != null ? user.getUserRole() : "N/A");
                    row.createCell(3).setCellValue(user.getLoginTime() != null
                            ? user.getLoginTime().toLocalDateTime().format(DATE_FORMATTER) : "N/A");
                    row.createCell(4).setCellValue(user.getLogoutTime() != null
                            ? user.getLogoutTime().toLocalDateTime().format(DATE_FORMATTER) : "N/A");
                    row.createCell(5).setCellValue(user.getStatus() != null ? user.getStatus() : "N/A");
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
