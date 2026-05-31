package models.mappers;

import models.Pasagjeri;
import models.dto.PasagjeriTableDTO; // Kjo duhet të implementojë IRequestDto
import models.mappers.IMapper;
import models.dto.IRequestDto;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class PasagjeriMapper implements IMapper<Pasagjeri> {

    // 1. Kjo është metoda që kërkon BaseRepository
    @Override
    public Pasagjeri getFromResultSet(ResultSet rs) {
        try {
            Pasagjeri p = new Pasagjeri();
            p.setIdPasagjerit(rs.getInt("id_pasagjerit"));
            p.setNumriPasaportes(rs.getString("numri_pasaportes"));
            p.setIdVenditShtetesise(rs.getInt("id_vendit_shtetesise"));
            p.setEmri(rs.getString("emri"));
            p.setMbiemri(rs.getString("mbiemri"));
            p.setDatelindja(rs.getDate("datelindja"));
            p.setGjinia(rs.getString("gjinia"));
            p.setEmail(rs.getString("email"));
            p.setTelefoni(rs.getString("telefoni"));
            p.setAdresa(rs.getString("adresa"));
            p.setPasaportaSkadimi(rs.getDate("pasaporta_skadimi"));
            return p;
        } catch (SQLException e) {
            throw new RuntimeException("Gabim gjatë mapimit nga ResultSet", e);
        }
    }

    // 2. Metoda nga interface që shton një pasagjer të ri nga DTO
    @Override
    public Pasagjeri fromDto(IRequestDto dto) {
        return fromDto(new Pasagjeri(), dto);
    }

    // 3. Metoda nga interface që përditëson një pasagjer ekzistues nga DTO
    @Override
    public Pasagjeri fromDto(Pasagjeri obj, IRequestDto dto) {
        // KËTU ËSHTË ZGJIDHJA: Nëse obj është null, krijo një objekt të ri Pasagjeri
        if (obj == null) {
            obj = new Pasagjeri();
        }

        // Këtu konvertojmë DTO-në në tipin konkret
        PasagjeriTableDTO pDto = (PasagjeriTableDTO) dto;

        obj.setNumriPasaportes(pDto.getNumriPasaportes());
        obj.setEmri(pDto.getEmri());
        obj.setMbiemri(pDto.getMbiemri());
        obj.setGjinia(pDto.getGjinia());
        obj.setEmail(pDto.getEmail());
        obj.setTelefoni(pDto.getTelefoni());
        obj.setAdresa(pDto.getAdresa());

        if (pDto.getDatelindja() != null) {
            obj.setDatelindja(java.sql.Date.valueOf(pDto.getDatelindja()));
        }
        if (pDto.getPasaportaSkadimi() != null) {
            obj.setPasaportaSkadimi(java.sql.Date.valueOf(pDto.getPasaportaSkadimi()));
        }

        return obj;
    }
}