package services;

import models.Perdoruesi;
import models.dto.LoginResponseDto;
import repository.UserRepository;

public class AuthService {
    private final UserRepository userRepo = new UserRepository();

    public LoginResponseDto login(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            return new LoginResponseDto(false, "Ju lutemi plotësoni të gjitha fushat.");
        }

        Perdoruesi user = userRepo.findByUsername(username);

        if (user == null) {
            return new LoginResponseDto(false, "Përdoruesi nuk ekziston.");
        }

        if (!user.isEshteAktiv()) {
            return new LoginResponseDto(false, "Llogaria është e çaktivizuar.");
        }

        if (!HashService.validatePassword(password, user.getPasswordHash())) {
            return new LoginResponseDto(false, "Fjalëkalim i gabuar.");
        }

        return new LoginResponseDto(true, "Login i suksesshëm.");
    }
}