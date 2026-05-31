package models.mappers;

import models.dto.ShtoPunonjesDTO;
import models.Stafi;
import java.sql.Date;

public class StafiMapper {

    public static Stafi toEntity(ShtoPunonjesDTO dto, int idRolit, String numriPunonjesit) {
        if (dto == null) {
            return null;
        }

        // Krijojmë modelin e plotë duke vendosur vlerat e konvertuara
        return new Stafi(
                0,                                               // idStafit (Autoincrement në DB)
                idRolit,                                         // idRolit të gjetur/krijuar
                null,                                            // idKompanise (Default Null)
                1,                                               // idAeroportit (Default 1 sipas të dhënave tuaja)
                dto.getEmri(),
                dto.getMbiemri(),
                numriPunonjesit,                                 // Numri unik i gjeneruar
                dto.getEmail(),
                dto.getTelefoni().isEmpty() ? null : dto.getTelefoni(),
                null,                                            // datelindja
                Date.valueOf(dto.getDataPunesimit()),            // Konvertimi i LocalDate -> java.sql.Date
                null,                                            // dataMbarimit
                true                                             // eshteAktiv
        );
    }
    public static models.Stafi toEntityFromEdit(models.dto.EditoPunonjesDTO dto, int idRolit) {
        if (dto == null) return null;

        models.Stafi stafi = new models.Stafi(dto.getIdStafit());
        stafi.setIdRolit(idRolit);
        stafi.setEmri(dto.getEmri());
        stafi.setMbiemri(dto.getMbiemri());
        stafi.setEmailPunes(dto.getEmail());
        stafi.setTelefoni(dto.getTelefoni().isEmpty() ? null : dto.getTelefoni());
        stafi.setEshteAktiv(dto.isEshteAktiv());
        // Nëse personi largohet nga puna (bëhet jo-aktiv), vendosim datën e mbarimit si sot
        if (!dto.isEshteAktiv()) {
            stafi.setDataMbarimit(java.sql.Date.valueOf(java.time.LocalDate.now()));
        }
        return stafi;
    }
}