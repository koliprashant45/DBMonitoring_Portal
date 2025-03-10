package com.dbmonitor.app.ui;

import com.dbmonitor.app.auth.AuthService;
import com.dbmonitor.app.auth.LoginView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
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
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.server.VaadinSession;

public class MainLayout extends AppLayout implements BeforeEnterObserver {
    private final H2 viewTitle;

    public MainLayout() {
        // App title
        H1 appTitle = new H1("DB Monitoring Portal");
        appTitle.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "var(--lumo-space-m)")
                .set("color", "white")
                .set("font-weight", "bold");

        // Tabs and drawer layout
        Tabs tabs = createVerticalTabs();
        Scroller scroller = new Scroller(tabs);
        scroller.getStyle().set("padding", "10px");

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

        // Logout button with icon
        Button logoutButton = new Button("Logout", VaadinIcon.SIGN_OUT.create(), event -> {
            VaadinSession.getCurrent().getSession().invalidate();
            getUI().ifPresent(ui -> ui.navigate(LoginView.class));
        });

        logoutButton.getStyle()
                .set("color", "#2A41D6")
                .set("border-radius", "6px")
                .set("padding", "5px 12px")
                .set("border", "1px solid #2A41D6")
                .set("font-weight", "bold");

        // Drawer toggle and dashboard title
        HorizontalLayout leftSection = new HorizontalLayout(toggle, viewTitle);
        leftSection.setAlignItems(FlexComponent.Alignment.CENTER);
        leftSection.setSpacing(false);
        leftSection.getStyle().set("margin", "0");

        // Logout button
        HorizontalLayout rightSection = new HorizontalLayout(logoutButton);
        rightSection.setAlignItems(FlexComponent.Alignment.CENTER);
        rightSection.getStyle().set("margin-right", "15px"); 

        // Main navbar layout
        HorizontalLayout navbarLayout = new HorizontalLayout(leftSection, rightSection);
        navbarLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        navbarLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        navbarLayout.setWidthFull();

        VerticalLayout viewHeader = new VerticalLayout(navbarLayout);
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

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!AuthService.isAuthenticated()) {
            event.forwardTo(LoginView.class);
        }
    }
}
