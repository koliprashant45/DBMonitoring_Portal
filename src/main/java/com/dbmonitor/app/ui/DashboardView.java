package com.dbmonitor.app.ui;

import com.dbmonitor.app.service.DashboardService;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@PageTitle("DB Monitoring Portal")
@Route(value = "dashboard", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PermitAll
public class DashboardView extends VerticalLayout {

    private final DashboardService dashboardService;

    @Autowired
    public DashboardView(DashboardService dashboardService) {
        this.dashboardService = dashboardService;

        setSpacing(false);
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        getStyle().set("text-align", "center");

        // Header
        H3 header = new H3("DB Monitoring Quick Insights");
        header.getStyle().set("font-size", "24px").set("font-weight", "bold");

        Hr divider = new Hr();
        divider.getStyle().set("margin", "10px 0");
        header.add(divider);

        add(header);

        // Add cards
        add(createJobStatusCard(), createDatabaseSizeCard(), createLoggedInUsersCard());
    }

    private Div createJobStatusCard() {
        Div card = createCard();
        card.add(new H3("Job Status (Last 24 Hours)"));

        Hr divider = new Hr();
        divider.getStyle().set("margin", "10px 0");
        card.add(divider);

        Map<String, Long> jobStatus = dashboardService.getJobStatusCounts();
        card.add(createStatusItem("✔️", "Completed:", jobStatus.getOrDefault("Completed", 0L)));
        card.add(createStatusItem("❌", "Failed:", jobStatus.getOrDefault("Failed", 0L)));
        card.add(createStatusItem("⏳", "Running:", jobStatus.getOrDefault("Running", 0L)));

        return card;
    }

    private Div createDatabaseSizeCard() {
        Div card = createCard();
        card.add(new H3("Database Size Monitoring"));

        Hr divider = new Hr();
        divider.getStyle().set("margin", "10px 0");
        card.add(divider);

        String maxDbSize = dashboardService.getMaxDatabaseSize();

        // Database size value centered
        Span dbSize = new Span(maxDbSize);
        dbSize.getStyle()
                .set("color", "#3498db")
                .set("font-size", "16px")
                .set("font-weight", "bold")
                .set("display", "block")
                .set("text-align", "center")
                .set("margin-bottom", "5px");

        // Label below the value
        Span label = new Span("(Max Database Size)");
        label.getStyle()
                .set("font-size", "14px")
                .set("font-weight", "normal")
                .set("display", "block")
                .set("text-align", "center")
                .set("color", "#666");

        card.add(dbSize, label);
        return card;
    }


    private Div createLoggedInUsersCard() {
        Div card = createCard();
        card.add(new H3("Logged-in Users"));

        Hr divider = new Hr();
        divider.getStyle().set("margin", "10px 0");
        card.add(divider);

        Map<String, Long> userCounts = dashboardService.getLoggedInUserCounts();
        card.add(createStatusItem("🟢", "Active:", userCounts.getOrDefault("Active", 0L)));
        card.add(createStatusItem("🔴", "Inactive:", userCounts.getOrDefault("Inactive", 0L)));
        card.add(createStatusItem("📜", "Logged Out:", userCounts.getOrDefault("LoggedOut", 0L)));

        return card;
    }

    private Div createCard() {
        Div card = new Div();
        card.getStyle()
                .set("border", "1px solid #ccc")
                .set("border-radius", "12px")
                .set("padding", "20px")
                .set("margin", "10px")
                .set("width", "400px")
                .set("box-shadow", "3px 3px 15px rgba(0, 0, 0, 0.1)")
                .set("background", "#ffffff");
        return card;
    }

    private Div createStatusItem(String icon, String label, long count) {
        Div item = new Div();
        item.getStyle().set("display", "flex").set("align-items", "center").set("gap", "8px");

        Span iconSpan = new Span(icon);
        iconSpan.getStyle().set("font-size", "18px");

        Span text = new Span(label + " " + count);
        text.getStyle().set("font-size", "16px").set("font-weight", "500");

        item.add(iconSpan, text);
        return item;
    }
}
