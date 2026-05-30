package app;

public enum ViewsEnum {
    LOGIN_VIEW("/views/LogIn_view.fxml"),
    HOME_VIEW("/views/Home_view.fxml"),
    SIGNUP_VIEW("/views/SignUp_view.fxml"),

    // Pamjet e reja të shtuara për navigim
    FLUTURIMET_VIEW("/views/Fluturimet_view.fxml"),
    REZERVIMET_VIEW("/views/Rezervimet_view.fxml"),
    BILETAT_VIEW("/views/Biletat_view.fxml"),
    NJOFTIMET_VIEW("/views/Njoftimet_view.fxml"),
    PROFILE_VIEW("/views/Profile_view.fxml");

    private final String viewPath;

    ViewsEnum(String viewPath) {
        this.viewPath = viewPath;
    }

    public String getValue() {
        return viewPath;
    }
}