package models.mappers;

import models.Linjat;
import models.dto.IRequestDto;
import models.dto.LinjaFormDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LinjaMapper implements IMapper<Linjat> {

    @Override
    public Linjat getFromResultSet(ResultSet rs) {
        try {
            Linjat l = new Linjat(
                    rs.getInt("id_linjes"),
                    rs.getInt("id_aeroportit_nisjes"),
                    rs.getInt("id_aeroportit_mbrrritjes"),
                    rs.getObject("distanca_km")        != null ? rs.getInt("distanca_km")          : null,
                    rs.getObject("koha_fluturimit_min") != null ? rs.getInt("koha_fluturimit_min")  : null,
                    rs.getBoolean("eshte_aktive")
            );
            // Joined fields
            try { l.setKodiIataNisjes(rs.getString("kodi_iata_nisjes")); }     catch (SQLException ignored) {}
            try { l.setEmriNisjes(rs.getString("emri_nisjes")); }               catch (SQLException ignored) {}
            try { l.setKodiIataMbrrritjes(rs.getString("kodi_iata_mbrrritjes")); } catch (SQLException ignored) {}
            try { l.setEmriMbrrritjes(rs.getString("emri_mbrrritjes")); }       catch (SQLException ignored) {}

            return l;
        } catch (SQLException e) {
            throw new RuntimeException("LinjaMapper: gabim mapping", e);
        }
    }

    @Override
    public Linjat fromDto(IRequestDto dto) {
        if (!(dto instanceof LinjaFormDto f))
            throw new IllegalArgumentException("LinjaMapper.fromDto: tip i papritur");

        return new Linjat(0, f.getIdAeroportitNisjes(), f.getIdAeroportitMbrrritjes(),
                f.getDistancaKm(), f.getKohaFluturimitMin(), f.isEshteAktive());
    }

    @Override
    public Linjat fromDto(Linjat obj, IRequestDto dto) {
        if (!(dto instanceof LinjaFormDto f))
            throw new IllegalArgumentException("LinjaMapper.fromDto(obj,dto): tip i papritur");

        obj.setIdAeroportitNisjes(f.getIdAeroportitNisjes());
        obj.setIdAeroportitMbrrritjes(f.getIdAeroportitMbrrritjes());
        obj.setDistancaKm(f.getDistancaKm());
        obj.setKohaFluturimitMin(f.getKohaFluturimitMin());
        obj.setEshteAktive(f.isEshteAktive());
        return obj;
    }
}