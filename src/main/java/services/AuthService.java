package services;

import models.Perdoruesi;
import models.dto.ForgotPasswordRequestDto;
import models.dto.ForgotPasswordResponseDto;
import models.dto.LoginResponseDto;
import models.mappers.ForgotPasswordMapper;
import repository.PasswordResetRepository;
import repository.UserRepository;

public class AuthService {
    private final UserRepository userRepo = new UserRepository();
    private final PasswordResetRepository passwordResetRepository = new PasswordResetRepository();
    private final ForgotPasswordMapper forgotPasswordMapper = new ForgotPasswordMapper();

    public LoginResponseDto login(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            return new LoginResponseDto(false, "Ju lutemi plotesoni te gjitha fushat.");
        }

        Perdoruesi user = userRepo.findByUsername(username);

        if (user == null) {
            return new LoginResponseDto(false, "Perdoruesi nuk ekziston.");
        }

        if (!user.isEshteAktiv()) {
            return new LoginResponseDto(false, "Llogaria eshte e caktivizuar.");
        }

        if (!HashService.validatePassword(password, user.getPasswordHash())) {
            return new LoginResponseDto(false, "Fjalekalim i gabuar.");
        }

        return new LoginResponseDto(true, "Login i suksesshem.");
    }

    public ForgotPasswordResponseDto resetPassword(ForgotPasswordRequestDto request) {
        if (request == null) {
            return new ForgotPasswordResponseDto(false, "Kerkesa nuk eshte valide.");
        }

        Perdoruesi requestUser = forgotPasswordMapper.fromDto(request);

        String username = requestUser.getUsername();
        String email = requestUser.getEmail();
        String newPassword = request.getNewPassword();
        String confirmPassword = request.getConfirmPassword();

        if (isBlank(username) || isBlank(email) || isBlank(newPassword) || isBlank(confirmPassword)) {
            return new ForgotPasswordResponseDto(false, "Ju lutemi plotesoni te gjitha fushat.");
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return new ForgotPasswordResponseDto(false, "Email adresa nuk eshte valide.");
        }

        if (newPassword.length() < 6) {
            return new ForgotPasswordResponseDto(false, "Fjalekalimi duhet te kete te pakten 6 karaktere.");
        }

        if (!newPassword.equals(confirmPassword)) {
            return new ForgotPasswordResponseDto(false, "Fjalekalimet nuk perputhen.");
        }

        Perdoruesi user = passwordResetRepository.findByUsernameAndEmail(username, email);

        if (user == null) {
            return new ForgotPasswordResponseDto(false, "Nuk u gjet llogari me kete username dhe email.");
        }

        if (!user.isEshteAktiv()) {
            return new ForgotPasswordResponseDto(false, "Llogaria eshte e caktivizuar.");
        }

        String passwordHash = HashService.generateHash(newPassword);
        boolean updated = passwordResetRepository.updatePassword(user.getIdPerdoruesit(), passwordHash);

        if (!updated) {
            return new ForgotPasswordResponseDto(false, "Fjalekalimi nuk u ndryshua. Provoni perseri.");
        }

        return new ForgotPasswordResponseDto(true, "Fjalekalimi u ndryshua me sukses. Tani mund te hyni ne sistem.");
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
