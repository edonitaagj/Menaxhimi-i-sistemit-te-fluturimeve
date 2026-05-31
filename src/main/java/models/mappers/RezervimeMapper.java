package models.mappers;

import models.dto.IRequestDto;
import models.dto.RezervimeTableDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class RezervimeMapper implements IMapper<RezervimeTableDto> {

    @Override
    public RezervimeTableDto getFromResultSet(ResultSet rs) {
        try {
            return new RezervimeTableDto(
                    rs.getInt("id_rezervimit"),
                    rs.getString("kodi_rezervimit"),
                    rs.getInt("id_pasagjerit"),
                    rs.getInt("id_fluturimit"),
                    rs.getString("pasagjeri"),
                    rs.getString("fluturimi_kodi"),
                    rs.getString("klasa"),
                    rs.getDouble("cmimi_total"),
                    rs.getString("monedha"),
                    rs.getString("statusi"),
                    rs.getString("statusi_pageses"),
                    formatTs(rs.getTimestamp("data_rezervimit")),
                    formatTs(rs.getTimestamp("data_anulimit")),
                    rs.getString("shenimet")
            );
        } catch (SQLException e) {
            throw new RuntimeException("Gabim gjatë mapimit të rezervimit", e);
        }
    }

    @Override
    public RezervimeTableDto fromDto(IRequestDto dto) {
        throw new UnsupportedOperationException("RezervimetTableDto nuk përdor input DTO në këtë view.");
    }

    @Override
    public RezervimeTableDto fromDto(RezervimeTableDto obj, IRequestDto dto) {
        throw new UnsupportedOperationException("RezervimetTableDto nuk përdor input DTO në këtë view.");
    }

    private String formatTs(Timestamp ts) {
        if (ts == null) return "-";
        return ts.toLocalDateTime().toString().replace('T', ' ').substring(0, 16);
    }
}