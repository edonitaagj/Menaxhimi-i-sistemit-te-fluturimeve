package models.mappers;

import models.Perdoruesi;
import models.dto.ForgotPasswordRequestDto;
import models.dto.IRequestDto;

import java.sql.ResultSet;

public class ForgotPasswordMapper implements IMapper<Perdoruesi> {

    private final UserMapper userMapper = new UserMapper();

    @Override
    public Perdoruesi getFromResultSet(ResultSet res) {
        return userMapper.getFromResultSet(res);
    }

    @Override
    public Perdoruesi fromDto(IRequestDto dto) {
        if (dto instanceof ForgotPasswordRequestDto forgotPasswordDto) {
            return new Perdoruesi(
                    0,
                    null,
                    "",
                    "",
                    forgotPasswordDto.getEmail(),
                    forgotPasswordDto.getNewPassword(),
                    "",
                    true,
                    0,
                    null,
                    forgotPasswordDto.getUsername()
            );
        }

        throw new IllegalArgumentException("Unsupported DTO type: " + dto.getClass().getSimpleName());
    }

    @Override
    public Perdoruesi fromDto(Perdoruesi obj, IRequestDto dto) {
        if (obj == null) {
            return fromDto(dto);
        }

        if (dto instanceof ForgotPasswordRequestDto forgotPasswordDto) {
            obj.setUsername(forgotPasswordDto.getUsername());
            obj.setEmail(forgotPasswordDto.getEmail());
            obj.setPasswordHash(forgotPasswordDto.getNewPassword());
            return obj;
        }

        throw new IllegalArgumentException("Unsupported DTO type: " + dto.getClass().getSimpleName());
    }
}
