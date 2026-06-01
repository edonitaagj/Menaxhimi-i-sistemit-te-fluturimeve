package services;

import app.I18n;
import models.Perdoruesi;
import models.dto.ForgotPasswordRequestDto;
import models.dto.ForgotPasswordResponseDto;
import models.mappers.ForgotPasswordMapper;
import repository.ForgotPasswordRepository;

public class ForgotPasswordService {

    private final ForgotPasswordRepository forgotPasswordRepository = new ForgotPasswordRepository();
    private final ForgotPasswordMapper forgotPasswordMapper = new ForgotPasswordMapper();

    public ForgotPasswordResponseDto resetPassword(ForgotPasswordRequestDto dto) {
        if (dto == null
                || dto.getUsername().isEmpty()
                || dto.getEmail().isEmpty()
                || dto.getNewPassword().isEmpty()
                || dto.getConfirmPassword().isEmpty()) {
            return ForgotPasswordResponseDto.error(text("forgotPassword.error.requiredFields"));
        }

        if (!dto.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return ForgotPasswordResponseDto.error(text("forgotPassword.error.invalidEmail"));
        }

        if (dto.getNewPassword().length() < 8) {
            return ForgotPasswordResponseDto.error(text("forgotPassword.error.passwordLength"));
        }

        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            return ForgotPasswordResponseDto.error(text("forgotPassword.error.passwordMismatch"));
        }

        Perdoruesi requestUser = forgotPasswordMapper.fromDto(dto);
        Perdoruesi user = forgotPasswordRepository.findByUsernameAndEmail(
                requestUser.getUsername(),
                requestUser.getEmail()
        );

        if (user == null) {
            return ForgotPasswordResponseDto.error(text("forgotPassword.error.userNotFound"));
        }

        if (!user.isEshteAktiv()) {
            return ForgotPasswordResponseDto.error(text("forgotPassword.error.inactiveAccount"));
        }

        if (HashService.validatePassword(dto.getNewPassword(), user.getPasswordHash())) {
            return ForgotPasswordResponseDto.error(text("forgotPassword.error.samePassword"));
        }

        String hashedPassword = HashService.generateHash(dto.getNewPassword());
        boolean updated = forgotPasswordRepository.updatePassword(user.getIdPerdoruesit(), hashedPassword);

        if (!updated) {
            return ForgotPasswordResponseDto.error(text("forgotPassword.error.updateFailed"));
        }

        return ForgotPasswordResponseDto.ok(text("forgotPassword.success.passwordUpdated"));
    }

    private String text(String key) {
        return I18n.getResourceBundle().getString(key);
    }
}
