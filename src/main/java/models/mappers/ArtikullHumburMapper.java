package models.mappers;

import models.ArtikujtHumbur;
import models.dto.ArtikullHumburRequestDto;
import models.dto.IRequestDto;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ArtikullHumburMapper implements IMapper<ArtikujtHumbur> {

    @Override
    public ArtikujtHumbur getFromResultSet(ResultSet res) {
        try {
            ArtikujtHumbur a = new ArtikujtHumbur();

            a.setIdArtikullit(res.getInt("id_artikullit"));
            a.setIdAeroportit(res.getInt("id_aeroportit"));
            if (res.wasNull()) {
                a.setIdAeroportit(0);
            }

            a.setPershkrimi(res.getString("pershkrimi"));
            a.setKategoria(res.getString("kategoria"));
            a.setDataGjetjes(res.getDate("data_gjetjes"));
            a.setVendiGjetjes(res.getString("vendi_gjetjes"));
            a.setStatusi(res.getString("statusi"));

            int idPasagjeri = res.getInt("id_pasagjerit_pronar");
            a.setIdPasagjeritPronar(res.wasNull() ? null : idPasagjeri);

            a.setIdStafitRaportues(res.getInt("id_stafit_raportues"));
            a.setFotoPath(res.getString("foto_path"));

            return a;
        } catch (SQLException e) {
            throw new RuntimeException("Gabim në ArtikullHumburMapper.getFromResultSet()", e);
        }
    }

    @Override
    public ArtikujtHumbur fromDto(IRequestDto dto) {
        ArtikullHumburRequestDto d = (ArtikullHumburRequestDto) dto;

        ArtikujtHumbur a = new ArtikujtHumbur();

        // DB ka vetëm një kolonë tekstuale "pershkrimi".
        // Prandaj i bashkojmë titullin + përshkrimin në një tekst të vetëm.
        String artikulli = safe(d.getArtikulli());
        String pershkrimi = safe(d.getPershkrimi());

        if (!artikulli.isBlank() && !pershkrimi.isBlank()) {
            a.setPershkrimi(artikulli + " - " + pershkrimi);
        } else if (!artikulli.isBlank()) {
            a.setPershkrimi(artikulli);
        } else {
            a.setPershkrimi(pershkrimi);
        }

        a.setVendiGjetjes(safe(d.getVendi()));
        a.setDataGjetjes(Date.valueOf(d.getDataGjetjes()));
        a.setStatusi(normalizeStatus(d.getStatusi()));
        a.setKategoria("tjetër"); // default i sigurt
        a.setFotoPath(null);

        return a;
    }

    @Override
    public ArtikujtHumbur fromDto(ArtikujtHumbur obj, IRequestDto dto) {
        return fromDto(dto);
    }

    private String safe(String v) {
        return v == null ? "" : v.trim();
    }

    private String normalizeStatus(String s) {
        if (s == null || s.trim().isBlank()) return "i_raportuar";
        return s.trim();
    }
}