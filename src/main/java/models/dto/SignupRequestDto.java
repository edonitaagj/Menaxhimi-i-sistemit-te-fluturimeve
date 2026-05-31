package models.dto;

public class SignupRequestDto implements IRequestDto{

    private String emri;
    private String mbiemri;
    private String username;
    private String email;
    private String password;
    private String confirmPassword;

    public SignupRequestDto(String emri, String mbiemri, String username,
                            String email, String password, String confirmPassword) {
        this.emri = emri;
        this.mbiemri = mbiemri;
        this.username = username;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getEmri() {
        return emri;
    }

    public String getMbiemri() {
        return mbiemri;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
}