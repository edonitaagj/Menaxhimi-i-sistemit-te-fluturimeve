package models.mappers;

import models.Avionet;
import models.dto.AvionetRequestDto;
import models.dto.IRequestDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AvionetMapper implements IMapper<Avionet> {

    @Override
    public Avionet getFromResultSet(ResultSet rs) {
        Avionet a = new Avionet();
        try {
            a.setIdAvionit(rs.getInt("id_avionit"));
            a.setIdKompanise(rs.getInt("id_kompanise"));
            a.setIdLlojit(rs.getInt("id_llojit"));
            a.setNumriRegjistrit(rs.getString("numri_regjistrit"));

            String vit = rs.getString("viti_prodhimit");
            a.setVitiProdhimit(vit != null ? Integer.parseInt(vit) : null);

            a.setStatusi(rs.getString("statusi"));
            a.setProdhuesi(rs.getString("prodhuesi"));
            a.setModeli(rs.getString("modeli"));
            a.setEmriKompanise(rs.getString("emri_kompanise"));
        } catch (SQLException e) {
            throw new RuntimeException("Gabim gjatë mapimit të avionit", e);
        }
        return a;
    }

    @Override
    public Avionet fromDto(IRequestDto dto) {
        if (!(dto instanceof AvionetRequestDto req)) {
            throw new IllegalArgumentException("DTO jo valide për AvionetMapper");
        }
        Avionet a = new Avionet();
        a.setIdKompanise(req.getIdKompanise() != null ? req.getIdKompanise() : 0);
        a.setIdLlojit(req.getIdLlojit() != null ? req.getIdLlojit() : 0);
        a.setNumriRegjistrit(req.getNumriRegjistrit());
        a.setVitiProdhimit(req.getVitiProdhimit());
        a.setStatusi(req.getStatusi());
        return a;
    }

    @Override
    public Avionet fromDto(Avionet obj, IRequestDto dto) {
        Avionet mapped = fromDto(dto);
        obj.setIdKompanise(mapped.getIdKompanise());
        obj.setIdLlojit(mapped.getIdLlojit());
        obj.setNumriRegjistrit(mapped.getNumriRegjistrit());
        obj.setVitiProdhimit(mapped.getVitiProdhimit());
        obj.setStatusi(mapped.getStatusi());
        return obj;
    }
}