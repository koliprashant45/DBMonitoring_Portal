package com.dbmonitor.app.auth;

import com.vaadin.flow.server.VaadinSession;

public class AuthService {
    public static boolean authenticate(String username, String password) {
        if ("admin".equals(username) && "password".equals(password)) {
            VaadinSession.getCurrent().setAttribute("user", username);
            return true;
        }
        return false;
    }

    public static boolean isAuthenticated() {
        return VaadinSession.getCurrent().getAttribute("user") != null;
    }
}
