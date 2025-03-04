package com.dbmonitor.app.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;

public class MainLayout extends AppLayout {
    private final H2 viewTitle;

    public MainLayout() {
        // App title
        H1 appTitle = new H1("DB Monitoring Portal");
        appTitle.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "var(--lumo-space-m)")
                .set("color", "white")
                .set("font-weight", "bold");

        // tabs and scroller
        Tabs tabs = createVerticalTabs();
        Scroller scroller = new Scroller(tabs);
        scroller.getStyle().set("padding", "10px");

        // blue color drawer background
        VerticalLayout drawerLayout = new VerticalLayout(appTitle, scroller);
        drawerLayout.getStyle()
                .set("background", "#2A41D6")
                .set("height", "100%")
                .set("color", "white")
                .set("padding", "10px");

        // Navbar components
        DrawerToggle toggle = new DrawerToggle();
        viewTitle = new H2("Dashboard");
        viewTitle.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");

        HorizontalLayout wrapper = new HorizontalLayout(toggle, viewTitle);
        wrapper.setAlignItems(FlexComponent.Alignment.CENTER);
        wrapper.setSpacing(false);

        VerticalLayout viewHeader = new VerticalLayout(wrapper);
        viewHeader.setPadding(false);
        viewHeader.setSpacing(false);

        //components layout
        addToDrawer(drawerLayout);
        addToNavbar(viewHeader);
        setPrimarySection(Section.DRAWER);
    }


    private Tabs createVerticalTabs() {
        // Tabs with icons and labels
        Tab dashboard = createTab(VaadinIcon.HOME, "Dashboard");
        Tab jobStatus = createTab(VaadinIcon.DASHBOARD, "Job Status");
        Tab dbSize = createTab(VaadinIcon.CHART, "Database Size Monitoring");
        Tab loggedInUsers = createTab(VaadinIcon.USER, "Logged-in Users");

        Tabs tabs = new Tabs(dashboard, jobStatus, dbSize, loggedInUsers);
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.getStyle()
                .set("background", "transparent")
                .set("color", "white")
                .set("font-size", "14px");

        tabs.addSelectedChangeListener(event -> updateView(tabs.getSelectedTab()));

        return tabs;
    }

    private Tab createTab(VaadinIcon icon, String title) {
        Span label = new Span(title);
        label.getStyle().set("color", "white").set("padding-left", "10px");

        var iconComponent = icon.create();
        iconComponent.getStyle().set("color", "white");

        HorizontalLayout tabLayout = new HorizontalLayout(iconComponent, label);
        tabLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        tabLayout.setSpacing(true);
        tabLayout.getStyle().set("padding", "10px");

        Tab tab = new Tab(tabLayout);
        tab.getElement().setAttribute("title", title);

        return tab;
    }

    private void updateView(Tab selectedTab) {
        String title = selectedTab.getElement().getAttribute("title");
        if (title != null) {
            viewTitle.setText(title);

            switch (title) {
                case "Dashboard":
                    getUI().ifPresent(ui -> ui.navigate(DashboardView.class));
                    break;
                case "Job Status":
                    getUI().ifPresent(ui -> ui.navigate(JobStatusView.class));
                    break;
                case "Database Size Monitoring":
                    getUI().ifPresent(ui -> ui.navigate(DBSizeView.class));
                    break;
                case "Logged-in Users":
                    getUI().ifPresent(ui -> ui.navigate(LoggedInUsersView.class));
                    break;
            }
        }
    }

}
