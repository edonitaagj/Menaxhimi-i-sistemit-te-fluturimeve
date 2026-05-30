package models.mappers;

import models.Perdoruesi;
import models.dto.IRequestDto;
import models.dto.LoginRequestDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements IMapper<Perdoruesi> {

    @Override
    public Perdoruesi getFromResultSet(ResultSet res) {
        try {
            return new Perdoruesi(
                    res.getInt("id_perdoruesit"),
                    res.getObject("id_stafit", Integer.class),
                    res.getString("emri"),
                    res.getString("mbiemri"),
                    res.getString("email"),
                    res.getString("password"),
                    res.getString("roli"),
                    res.getBoolean("eshte_aktiv"),
                    res.getInt("tentativa_login"),
                    res.getTimestamp("last_login"),
                    res.getString("username")
            );
        } catch (SQLException e) {
            throw new RuntimeException("Error mapping Perdoruesi", e);
        }
    }

    @Override
    public Perdoruesi fromDto(IRequestDto dto) {
        if (dto instanceof LoginRequestDto) {
            LoginRequestDto loginDto = (LoginRequestDto) dto;

            return new Perdoruesi(
                    0,
                    loginDto.getUsername(),
                    loginDto.getPassword()
            );
        }

        throw new IllegalArgumentException("Unsupported DTO type: " + dto.getClass().getSimpleName());
    }

    @Override
    public Perdoruesi fromDto(Perdoruesi obj, IRequestDto dto) {
        throw new UnsupportedOperationException("fromDto(obj, dto) is not implemented yet.");
    }
}