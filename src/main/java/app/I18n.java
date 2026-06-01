package app;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18n {
    private static Locale locale = Locale.ENGLISH;
    private static final String BASE_NAME = "i18n.messages";

    private I18n() {}

    public static void setLocale(Locale locale) {
        I18n.locale = locale;
    }

    public static Locale getLocale() {
        return locale;
    }

    public static ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle(BASE_NAME, locale);
    }
}
