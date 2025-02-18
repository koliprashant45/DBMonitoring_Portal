package com.dbmonitor.app.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MainLayout extends AppLayout {

    public MainLayout() {
        DrawerToggle toggle = new DrawerToggle();

        // App Title
        H1 title = new H1("DB Monitoring Portal");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        SideNav nav = createSideNav();

        Scroller scroller = new Scroller(nav);
        scroller.setClassName(LumoUtility.Padding.SMALL);

        addToDrawer(scroller);
        addToNavbar(toggle, title);
    }

    private SideNav createSideNav() {
        SideNav sideNav = new SideNav();
        sideNav.addItem(
                new SideNavItem("Dashboard", String.valueOf(DashboardView.class), VaadinIcon.HOME.create()),
                new SideNavItem("Job Status", JobStatusView.class, VaadinIcon.DASHBOARD.create()),
                new SideNavItem("Database Size Monitoring", String.valueOf(DBSizeView.class), VaadinIcon.CHART.create()),
                new SideNavItem("Logged-in Users", String.valueOf(LoggedInUsersView.class), VaadinIcon.USER.create())
        );
        return sideNav;
    }
}
