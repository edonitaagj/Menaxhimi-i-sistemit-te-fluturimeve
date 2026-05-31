package models.mappers;

import models.KompaniteAjrore;
import models.dto.IRequestDto;
import models.dto.KompaniaFormDto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class KompaniaMapper implements IMapper<KompaniteAjrore> {

    @Override
    public KompaniteAjrore getFromResultSet(ResultSet rs) {
        try {
            KompaniteAjrore k = new KompaniteAjrore(
                    rs.getInt("id_kompanise"),
                    rs.getInt("id_vendit"),
                    rs.getString("kodi_iata"),
                    rs.getString("kodi_icao"),
                    rs.getString("emri"),
                    rs.getString("emri_i_shkurter"),
                    rs.getString("faqja_web"),
                    rs.getString("telefoni"),
                    rs.getBoolean("eshte_aktive")
            );
            // Joined field — silent skip nëse mungon në query
            try { k.setEmriIShkurter(rs.getString("emri_shkurter")); }
            catch (SQLException ignored) {}

            return k;
        } catch (SQLException e) {
            throw new RuntimeException("KompaniaMapper: gabim mapping", e);
        }
    }

    /**
     * fromDto(dto) — krijon KompaniaAjrore të re nga forma (id=0).
     */
    @Override
    public KompaniteAjrore fromDto(IRequestDto dto) {
        if (!(dto instanceof KompaniaFormDto f))
            throw new IllegalArgumentException("KompaniaMapper.fromDto: tip i papritur");

        return new KompaniteAjrore(
                0,
                f.getIdVendit(),
                f.getKodiIata(),
                f.getKodiIcao(),
                f.getEmri(),
                f.getEmriIShkurter(),
                f.getFaqjaWeb(),
                f.getTelefoni(),
                f.isEshteAktive()
        );
    }

    /**
     * fromDto(obj, dto) — aplikon ndryshimet nga forma mbi objektin ekzistues.
     * Ruhet idKompanise dhe çdo fushë tjetër që nuk ndryshohet nga forma.
     */
    @Override
    public KompaniteAjrore fromDto(KompaniteAjrore obj, IRequestDto dto) {
        if (!(dto instanceof KompaniaFormDto f))
            throw new IllegalArgumentException("KompaniaMapper.fromDto(obj,dto): tip i papritur");

        obj.setEmri(f.getEmri());
        obj.setKodiIata(f.getKodiIata());
        obj.setKodiIcao(f.getKodiIcao());
        obj.setEmriIShkurter(f.getEmriIShkurter());
        obj.setIdVendit(f.getIdVendit());
        obj.setFaqjaWeb(f.getFaqjaWeb());
        obj.setTelefoni(f.getTelefoni());
        obj.setEshteAktive(f.isEshteAktive());
        return obj;
    }
}