package app;

public enum ViewsEnum {
    LOGIN_VIEW("/views/LogIn_view.fxml"),
    HOME_VIEW("/views/Home_view.fxml"),
    SIGNUP_VIEW("/views/SignUp_view.fxml");

    private final String viewPath;

    ViewsEnum(String viewPath) {
        this.viewPath = viewPath;
    }

    public String getValue() {
        return viewPath;
    }
}