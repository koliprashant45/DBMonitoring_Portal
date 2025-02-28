package com.dbmonitor.app.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MainLayout extends AppLayout {

    public MainLayout() {
        DrawerToggle toggle = new DrawerToggle();

        // App Title
        H1 title = new H1("DB Monitoring Portal");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        Tabs tabs = createVerticalTabs();

        Scroller scroller = new Scroller(tabs);
        scroller.setClassName(LumoUtility.Padding.SMALL);

        addToDrawer(scroller);
        addToNavbar(toggle, title);
    }

    private Tabs createVerticalTabs() {
        // Tabs with icons and labels
        Tab dashboard = new Tab(VaadinIcon.HOME.create(), new Span("Dashboard"));
        Tab jobStatus = new Tab(VaadinIcon.DASHBOARD.create(), new Span("Job Status"));
        Tab dbSize = new Tab(VaadinIcon.CHART.create(), new Span("Database Size Monitoring"));
        Tab loggedInUsers = new Tab(VaadinIcon.USER.create(), new Span("Logged-in Users"));

        // Tabs component and set orientation
        Tabs tabs = new Tabs(dashboard, jobStatus, dbSize, loggedInUsers);
        tabs.setOrientation(Tabs.Orientation.VERTICAL);

        // navigation logic
        tabs.addSelectedChangeListener(event -> {
            Tab selectedTab = tabs.getSelectedTab();
            if (selectedTab.equals(dashboard)) {
                getUI().ifPresent(ui -> ui.navigate(String.valueOf(DashboardView.class)));
            } else if (selectedTab.equals(jobStatus)) {
                getUI().ifPresent(ui -> ui.navigate(JobStatusView.class));
            } else if (selectedTab.equals(dbSize)) {
                getUI().ifPresent(ui -> ui.navigate(DBSizeView.class));
            } else if (selectedTab.equals(loggedInUsers)) {
                getUI().ifPresent(ui -> ui.navigate(LoggedInUsersView.class));
            }
        });

        return tabs;
    }
}