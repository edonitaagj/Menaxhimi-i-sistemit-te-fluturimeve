package models.dto;

public class ForgotPasswordRequestDto implements IRequestDto {

    private final String username;
    private final String email;
    private final String newPassword;
    private final String confirmPassword;

    public ForgotPasswordRequestDto(String username,
                                    String email,
                                    String newPassword,
                                    String confirmPassword) {
        this.username = username != null ? username.trim() : "";
        this.email = email != null ? email.trim() : "";
        this.newPassword = newPassword != null ? newPassword : "";
        this.confirmPassword = confirmPassword != null ? confirmPassword : "";
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
}
