package repository;

import models.dto.LookupDto;
import models.dto.MirembajtjaRequestDto;
import models.dto.MirembajtjaTableDto;
import models.dto.StaffLookupDto;
import services.DatabaseService;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MirembajtjaRepository {

    public List<MirembajtjaTableDto> getByAvionId(int idAvionit) {
        String sql = """
            SELECT m.id_mirembajtjes, m.id_avionit, m.lloji_sherbimit, m.data_fillimit,
                   m.data_mbarimit, m.pershkrimi_punes, m.kostoja, m.id_stafit_pergjegjes,
                   m.statusi,
                   CONCAT(s.emri, ' ', s.mbiemri, ' (', s.numri_punonjesit, ')') AS emri_stafit
            FROM mirembajtja_avioneve m
            INNER JOIN stafi s ON s.id_stafit = m.id_stafit_pergjegjes
            WHERE m.id_avionit = ?
            ORDER BY m.data_fillimit DESC
        """;

        List<MirembajtjaTableDto> list = new ArrayList<>();

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idAvionit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new MirembajtjaTableDto(
                            rs.getInt("id_mirembajtjes"),
                            rs.getInt("id_avionit"),
                            rs.getString("lloji_sherbimit"),
                            formatTs(rs.getTimestamp("data_fillimit")),
                            formatTs(rs.getTimestamp("data_mbarimit")),
                            rs.getString("pershkrimi_punes"),
                            rs.getBigDecimal("kostoja") != null ? rs.getBigDecimal("kostoja").doubleValue() : 0.0,
                            rs.getInt("id_stafit_pergjegjes"),
                            rs.getString("statusi"),
                            rs.getString("emri_stafit")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Gabim gjatë marrjes së mirëmbajtjes: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    public boolean insert(MirembajtjaRequestDto dto) {
        String sql = """
            INSERT INTO mirembajtja_avioneve
            (id_avionit, lloji_sherbimit, data_fillimit, data_mbarimit, pershkrimi_punes, kostoja, id_stafit_pergjegjes, statusi)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, dto.getIdAvionit());
            ps.setString(2, dto.getLlojiSherbimit());
            ps.setTimestamp(3, Timestamp.valueOf(dto.getDataFillimit()));

            if (dto.getDataMbarimit() != null) {
                ps.setTimestamp(4, Timestamp.valueOf(dto.getDataMbarimit()));
            } else {
                ps.setNull(4, Types.TIMESTAMP);
            }

            ps.setString(5, dto.getPershkrimiPunes());
            if (dto.getKostoja() != null) {
                ps.setDouble(6, dto.getKostoja());
            } else {
                ps.setNull(6, Types.DECIMAL);
            }
            ps.setInt(7, dto.getIdStafitPergjegjes());
            ps.setString(8, dto.getStatusi() != null ? dto.getStatusi() : "në_proces");

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gabim gjatë insert të mirëmbajtjes: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<StaffLookupDto> getStafiLookup() {
        String sql = """
            SELECT id_stafit,
                   CONCAT(emri, ' ', mbiemri, ' (', numri_punonjesit, ')') AS label
            FROM stafi
            WHERE eshte_aktiv = 1
            ORDER BY emri, mbiemri
        """;

        List<StaffLookupDto> list = new ArrayList<>();

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new StaffLookupDto(rs.getInt("id_stafit"), rs.getString("label")));
            }
        } catch (SQLException e) {
            System.err.println("Gabim në lookup stafit: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    public List<LookupDto> getLlojetSherbimit() {
        List<LookupDto> list = new ArrayList<>();
        list.add(new LookupDto(1, "kontroll_rutinë"));
        list.add(new LookupDto(2, "riparim_emergjent"));
        list.add(new LookupDto(3, "ndërrim_pjesësh"));
        list.add(new LookupDto(4, "inspektim_vjetor"));
        return list;
    }

    public List<LookupDto> getStatusetMirembajtjes() {
        List<LookupDto> list = new ArrayList<>();
        list.add(new LookupDto(1, "në_proces"));
        list.add(new LookupDto(2, "e_përfunduar"));
        list.add(new LookupDto(3, "e_anuluar"));
        return list;
    }

    private String formatTs(Timestamp ts) {
        return ts == null ? "-" : ts.toLocalDateTime().toString().replace('T', ' ').substring(0, 16);
    }
}