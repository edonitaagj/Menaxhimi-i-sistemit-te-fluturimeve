package app;

import models.Perdoruesi;

public class SessionManager {

    private static Perdoruesi currentUser;
    private static Integer pendingReservationId;
    private static String pendingReservationCode;

    public static void login(Perdoruesi user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
        pendingReservationId = null;
        pendingReservationCode = null;
    }

    public static Perdoruesi getCurrentUser() {
        return currentUser;
    }

    public static void setPendingReservationId(Integer id) {
        pendingReservationId = id;
    }

    public static Integer getPendingReservationId() {
        return pendingReservationId;
    }

    public static void setPendingReservationCode(String code) {
        pendingReservationCode = code;
    }

    public static String getPendingReservationCode() {
        return pendingReservationCode;
    }
}