package app;

import models.Perdoruesi;

public class SessionManager {
    private static Perdoruesi currentUser;

    private SessionManager() {}

    public static void login(Perdoruesi user) {
        SessionManager.currentUser = user;
    }

    public static void logout() {
        SessionManager.currentUser = null;
    }

    public static Perdoruesi getCurrentUser() {
        return SessionManager.currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static String getCurrentRole() {
        return currentUser == null ? null : currentUser.getRoli();
    }
}