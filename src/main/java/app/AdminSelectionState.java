package app;

public final class AdminSelectionState {
    private static Integer selectedAvionId;
    private static String selectedAvionRegjistri;

    private AdminSelectionState() {}

    public static Integer getSelectedAvionId() {
        return selectedAvionId;
    }

    public static void setSelectedAvionId(Integer selectedAvionId) {
        AdminSelectionState.selectedAvionId = selectedAvionId;
    }

    public static String getSelectedAvionRegjistri() {
        return selectedAvionRegjistri;
    }

    public static void setSelectedAvionRegjistri(String selectedAvionRegjistri) {
        AdminSelectionState.selectedAvionRegjistri = selectedAvionRegjistri;
    }

    public static void clear() {
        selectedAvionId = null;
        selectedAvionRegjistri = null;
    }
}