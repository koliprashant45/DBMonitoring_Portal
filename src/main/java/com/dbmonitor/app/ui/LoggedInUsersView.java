package com.dbmonitor.app.ui;

import com.dbmonitor.app.model.LoggedInUser;
import com.dbmonitor.app.service.LoggedInUserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

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

        add(new HorizontalLayout(roleFilter, statusFilter, usernameSearch), userGrid);

        setSizeFull();
        userGrid.setSizeFull();
    }

    private void configureFilters() {
        // User Role Filter
        roleFilter.setLabel("Filter by Role");
        roleFilter.setItems("All", "Admin", "User", "Developer", "Support Engineer");
        roleFilter.setValue("All");
        roleFilter.addValueChangeListener(e -> applyFilters());

        // Status Filter
        statusFilter.setLabel("Filter by Status");
        statusFilter.setItems("All", "Active", "Inactive", "Logged Out");
        statusFilter.setValue("All");
        statusFilter.addValueChangeListener(e -> applyFilters());

        // Search by Username
        usernameSearch.setPlaceholder("Enter username");
        usernameSearch.setValueChangeMode(ValueChangeMode.EAGER);
        usernameSearch.addValueChangeListener(e -> applyFilters());

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
        userGrid.addColumn(LoggedInUser::getUserId)
                .setHeader("User ID")
                .setWidth("80px")
                .setFlexGrow(0);

        userGrid.addColumn(LoggedInUser::getUsername)
                .setHeader("Username")
                .setResizable(true);

        userGrid.addColumn(LoggedInUser::getUserRole)
                .setHeader("User Role")
                .setResizable(true)
                .setSortable(true);

        userGrid.addColumn(user -> user.getLoginTime() != null
                        ? user.getLoginTime().toLocalDateTime().format(DATE_FORMATTER) : "N/A")
                .setHeader("Login Time")
                .setResizable(true)
                .setSortable(true);

        userGrid.addColumn(user -> user.getLogoutTime() != null
                        ? user.getLogoutTime().toLocalDateTime().format(DATE_FORMATTER) : "N/A")
                .setHeader("Logout Time")
                .setResizable(true)
                .setSortable(true);

        userGrid.addColumn(LoggedInUser::getStatus)
                .setHeader("Status")
                .setResizable(true)
                .setSortable(true);

        // Terminate Button with Confirmation Dialog
        userGrid.addComponentColumn(user -> {
            Button terminateButton = new Button("Terminate", event -> showConfirmationDialog(user));
            terminateButton.setTooltipText("Terminate user");
            terminateButton.getStyle()
                    .set("background-color", "#ff3333")
                    .set("color", "white");

            String status = user.getStatus() != null ? user.getStatus().toLowerCase() : "";
            boolean isActive = "active".equals(status);
            terminateButton.setEnabled(isActive);

            if (!isActive) {
                terminateButton.getStyle()
                        .set("background-color", "#cccccc")
                        .set("color", "#666666");
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
        confirmButton.getStyle()
                .set("background-color", "#0000ff")
                .set("color", "white");

        Button cancelButton = new Button("No", event -> dialog.close());
        cancelButton.getStyle()
                .set("background-color", "#808080")
                .set("color", "white");

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
}
