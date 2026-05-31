package models.mappers;

import models.MirembajtjaAvioneve;
import models.dto.IRequestDto;
import models.dto.MirembajtjaRequestDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MirembajtjaMapper implements IMapper<MirembajtjaAvioneve> {

    @Override
    public MirembajtjaAvioneve getFromResultSet(ResultSet rs) {
        MirembajtjaAvioneve m = new MirembajtjaAvioneve();
        try {
            m.setIdMirembajtjes(rs.getInt("id_mirembajtjes"));
            m.setIdAvionit(rs.getInt("id_avionit"));
            m.setLlojiSherbimit(rs.getString("lloji_sherbimit"));
            m.setDataFillimit(rs.getTimestamp("data_fillimit") != null ? rs.getTimestamp("data_fillimit").toLocalDateTime() : null);
            m.setDataMbarimit(rs.getTimestamp("data_mbarimit") != null ? rs.getTimestamp("data_mbarimit").toLocalDateTime() : null);
            m.setPershkrimiPunes(rs.getString("pershkrimi_punes"));

            var bd = rs.getBigDecimal("kostoja");
            m.setKostoja(bd != null ? bd.doubleValue() : 0.0);

            m.setIdStafitPergjegjes(rs.getInt("id_stafit_pergjegjes"));
            m.setStatusi(rs.getString("statusi"));
            m.setEmriStafit(rs.getString("emri_stafit"));
        } catch (SQLException e) {
            throw new RuntimeException("Gabim gjatë mapimit të mirëmbajtjes", e);
        }
        return m;
    }

    @Override
    public MirembajtjaAvioneve fromDto(IRequestDto dto) {
        if (!(dto instanceof MirembajtjaRequestDto req)) {
            throw new IllegalArgumentException("DTO jo valide për MirembajtjaMapper");
        }
        MirembajtjaAvioneve m = new MirembajtjaAvioneve();
        m.setIdAvionit(req.getIdAvionit() != null ? req.getIdAvionit() : 0);
        m.setLlojiSherbimit(req.getLlojiSherbimit());
        m.setDataFillimit(req.getDataFillimit());
        m.setDataMbarimit(req.getDataMbarimit());
        m.setPershkrimiPunes(req.getPershkrimiPunes());
        m.setKostoja(req.getKostoja() != null ? req.getKostoja() : 0.0);
        m.setIdStafitPergjegjes(req.getIdStafitPergjegjes() != null ? req.getIdStafitPergjegjes() : 0);
        m.setStatusi(req.getStatusi());
        return m;
    }

    @Override
    public MirembajtjaAvioneve fromDto(MirembajtjaAvioneve obj, IRequestDto dto) {
        MirembajtjaAvioneve mapped = fromDto(dto);
        obj.setIdAvionit(mapped.getIdAvionit());
        obj.setLlojiSherbimit(mapped.getLlojiSherbimit());
        obj.setDataFillimit(mapped.getDataFillimit());
        obj.setDataMbarimit(mapped.getDataMbarimit());
        obj.setPershkrimiPunes(mapped.getPershkrimiPunes());
        obj.setKostoja(mapped.getKostoja());
        obj.setIdStafitPergjegjes(mapped.getIdStafitPergjegjes());
        obj.setStatusi(mapped.getStatusi());
        return obj;
    }
}