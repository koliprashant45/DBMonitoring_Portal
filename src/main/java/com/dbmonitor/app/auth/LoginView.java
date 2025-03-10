package com.dbmonitor.app.auth;

import com.dbmonitor.app.ui.DashboardView;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

@Route("login")
@PageTitle("Login | DB Monitoring Portal")
public class LoginView extends VerticalLayout {

    public LoginView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        // background image
        getStyle().set("background-image", "url('themes/mytheme/images/Background.jpg')")
                .set("background-size", "cover")
                .set("background-position", "center")
                .set("background-repeat", "no-repeat");

        // login form
        LoginForm loginForm = new LoginForm();
        loginForm.getElement().getThemeList().add("dark");

        loginForm.addLoginListener(event -> {
            if (AuthService.authenticate(event.getUsername(), event.getPassword())) {
                VaadinSession.getCurrent().setAttribute("user", event.getUsername());
                getUI().ifPresent(ui -> ui.navigate(DashboardView.class));
            } else {
                loginForm.setError(true);
            }
        });

        loginForm.getElement().setAttribute("no-autofocus", "");

        add(loginForm);
    }
}
