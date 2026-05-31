package app;

public enum ViewsEnum {
    LOGIN_VIEW("/views/LogIn_view.fxml"),
    HOME_VIEW("/views/Home_view.fxml"),
    SIGNUP_VIEW("/views/SignUp_view.fxml"),

    REZERVIMET_VIEW("/views/Rezervimet_view.fxml"),
    BILETAT_VIEW("/views/Biletat_view.fxml"),
    NJOFTIMET_VIEW("/views/Njoftimet_view.fxml"),
    PROFIL_VIEW("/views/Profil_view.fxml"),
    PAGESAT_VIEW("/views/Pagesat_view.fxml"),
    SHTO_REZERVIM("/views/ShtoRezervim_view.fxml"),

    ADMIN_VIEW("/views/Admin_view.fxml"),
    ADMIN_ARTIKUJT_HUMBUR("/views/AdminArtikujtHumbur.fxml"),
    ADMIN_AVIONET("/views/AdminAvionet.fxml"),
    ADMIN_REZERVIMET("/views/AdminRezervimet.fxml"),
    ADMIN_FLUTURIMET("/views/AdminFluturimet.fxml"),
    ADMIN_STAFI("/views/AdminStafi.fxml"),
    ADMIN_PASAGJERIT("/views/AdminPasagjerit.fxml"),
    ADMIN_KOMPANITE("/views/AdminKompanite.fxml"),

    ADMIN_SHTO_AVION("/views/AdminShtoAvion_view.fxml"),
    ADMIN_SHTO_MIREMBAJTJE("/views/AdminShtoMirembajtje_view.fxml");

    private final String viewPath;

    ViewsEnum(String viewPath) {
        this.viewPath = viewPath;
    }

    public String getValue() {
        return viewPath;
    }
}