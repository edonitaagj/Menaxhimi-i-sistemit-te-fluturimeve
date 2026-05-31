package models.mappers;

import models.Stafi;
import models.dto.IRequestDto;
import models.dto.ShtoPunonjesDTO;
import models.dto.EditoPunonjesDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;

public class StafiMapper implements IMapper<Stafi> {

    // 1. Leximi nga Databaza (ResultSet -> Entity)
    @Override
    public Stafi getFromResultSet(ResultSet res) {
        try {
            return new Stafi(
                    res.getInt("id_stafit"),
                    res.getInt("id_rolit"),
                    res.getObject("id_kompanise") != null ? res.getInt("id_kompanise") : null,
                    res.getObject("id_aeroportit") != null ? res.getInt("id_aeroportit") : null,
                    res.getString("emri"),
                    res.getString("mbiemri"),
                    res.getString("numri_punonjesit"),
                    res.getString("email_punes"),
                    res.getString("telefoni"),
                    res.getDate("datelindja"),
                    res.getDate("data_fillimit"),
                    res.getDate("data_mbarimit"),
                    res.getBoolean("eshte_aktiv")
            );
        } catch (SQLException e) {
            throw new RuntimeException("Gabim gjatë konvertimit të ResultSet në Stafi", e);
        }
    }

    // 2. Konvertimi për Shtim të Ri (DTO -> Entity)
    @Override
    public Stafi fromDto(IRequestDto dto) {
        if (dto instanceof ShtoPunonjesDTO) {
            ShtoPunonjesDTO shtoDto = (ShtoPunonjesDTO) dto;

            // Gjenerojmë një numër unik ose e lëmë të menaxhohet nga kontrollori
            String numriPunonjesit = "STF-" + System.currentTimeMillis() % 100000;

            return new Stafi(
                    0,
                    0, // Do të vendoset dinamikisht në Repository
                    null,
                    1, // Default Aeroporti ID 1
                    shtoDto.getEmri(),
                    shtoDto.getMbiemri(),
                    numriPunonjesit,
                    shtoDto.getEmail(),
                    shtoDto.getTelefoni().isEmpty() ? null : shtoDto.getTelefoni(),
                    null,
                    Date.valueOf(shtoDto.getDataPunesimit()),
                    null,
                    true
            );
        }
        return null;
    }

    // 3. Konvertimi për Editim / Përditësim (Ekzistuesi + DTO -> Entity e ndryshuar)
    @Override
    public Stafi fromDto(Stafi obj, IRequestDto dto) {
        if (dto instanceof EditoPunonjesDTO) {
            EditoPunonjesDTO editoDto = (EditoPunonjesDTO) dto;

            if (obj == null) {
                obj = new Stafi(editoDto.getIdStafit());
            }

            obj.setEmri(editoDto.getEmri());
            obj.setMbiemri(editoDto.getMbiemri());
            obj.setEmailPunes(editoDto.getEmail());
            obj.setTelefoni(editoDto.getTelefoni().isEmpty() ? null : editoDto.getTelefoni());
            obj.setEshteAktiv(editoDto.isEshteAktiv());

            if (!editoDto.isEshteAktiv()) {
                obj.setDataMbarimit(Date.valueOf(LocalDate.now()));
            } else {
                obj.setDataMbarimit(null);
            }
            return obj;
        }
        return obj;
    }
}