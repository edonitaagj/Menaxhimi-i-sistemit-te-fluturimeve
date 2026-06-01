package models.mappers;

import models.Perdoruesi;
import models.dto.ForgotPasswordRequestDto;
import models.dto.IRequestDto;

import java.sql.ResultSet;

public class ForgotPasswordMapper implements IMapper<Perdoruesi> {

    @Override
    public Perdoruesi getFromResultSet(ResultSet res) {
        throw new UnsupportedOperationException("ForgotPasswordMapper does not map ResultSet directly.");
    }

    @Override
    public Perdoruesi fromDto(IRequestDto dto) {
        if (dto instanceof ForgotPasswordRequestDto request) {
            Perdoruesi user = new Perdoruesi(
                    0,
                    normalize(request.getUsername()),
                    request.getNewPassword()
            );
            user.setEmail(normalize(request.getEmail()));
            return user;
        }

        throw new IllegalArgumentException("Unsupported DTO type: " + dto.getClass().getSimpleName());
    }

    @Override
    public Perdoruesi fromDto(Perdoruesi obj, IRequestDto dto) {
        if (dto instanceof ForgotPasswordRequestDto request) {
            obj.setUsername(normalize(request.getUsername()));
            obj.setEmail(normalize(request.getEmail()));
            obj.setPasswordHash(request.getNewPassword());
            return obj;
        }

        throw new IllegalArgumentException("Unsupported DTO type: " + dto.getClass().getSimpleName());
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
