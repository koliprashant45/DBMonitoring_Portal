package com.dbmonitor.app.ui;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainLayout.class)
@PreserveOnRefresh
public class MainView extends VerticalLayout {

    public MainView() {
        add(new H1("Database Monitoring Portal"));
    }
}
