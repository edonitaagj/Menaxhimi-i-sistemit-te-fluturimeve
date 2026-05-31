package repository;

import models.ArtikujtHumbur;
import models.mappers.ArtikullHumburMapper;
import services.DatabaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtikujtHumburRepository {

    private final ArtikullHumburMapper mapper = new ArtikullHumburMapper();

    private static final String SELECT_BASE = """
        SELECT
            id_artikullit,
            id_aeroportit,
            pershkrimi,
            kategoria,
            data_gjetjes,
            vendi_gjetjes,
            statusi,
            id_pasagjerit_pronar,
            id_stafit_raportues,
            foto_path
        FROM artikujt_humbur
        """;

    public ArtikujtHumbur create(ArtikujtHumbur obj) {
        String sql = """
            INSERT INTO artikujt_humbur
            (id_aeroportit, pershkrimi, kategoria, data_gjetjes, vendi_gjetjes, statusi, id_pasagjerit_pronar, id_stafit_raportues, foto_path)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, obj.getIdAeroportit());
            ps.setString(2, obj.getPershkrimi());
            ps.setString(3, obj.getKategoria() == null || obj.getKategoria().isBlank() ? "tjetër" : obj.getKategoria());
            ps.setDate(4, obj.getDataGjetjes());
            ps.setString(5, obj.getVendiGjetjes());
            ps.setString(6, obj.getStatusi() == null || obj.getStatusi().isBlank() ? "i_raportuar" : obj.getStatusi());

            if (obj.getIdPasagjeritPronar() == null) {
                ps.setNull(7, Types.INTEGER);
            } else {
                ps.setInt(7, obj.getIdPasagjeritPronar());
            }

            ps.setInt(8, obj.getIdStafitRaportues());
            ps.setString(9, obj.getFotoPath());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Insert dështoi, nuk u krijua asnjë rresht.");
            }

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    obj.setIdArtikullit(keys.getInt(1));
                }
            }

            return obj;

        } catch (SQLException e) {
            throw new RuntimeException("Gabim gjatë create() në artikujt_humbur", e);
        }
    }

    public List<ArtikujtHumbur> findAll() {
        String sql = SELECT_BASE + " ORDER BY data_gjetjes DESC, id_artikullit DESC";
        return executeQueryList(sql);
    }

    public List<ArtikujtHumbur> search(String keyword) {
        String sql = SELECT_BASE + """
            WHERE pershkrimi LIKE ?
               OR vendi_gjetjes LIKE ?
               OR statusi LIKE ?
               OR kategoria LIKE ?
            ORDER BY data_gjetjes DESC, id_artikullit DESC
            """;

        String k = "%" + keyword.trim() + "%";
        return executeQueryList(sql, k, k, k, k);
    }

    public Integer findAeroportIdByStafId(int idStafit) {
        String sql = "SELECT id_aeroportit FROM stafi WHERE id_stafit = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idStafit);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int val = rs.getInt("id_aeroportit");
                    return rs.wasNull() ? null : val;
                }
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Gabim duke lexuar id_aeroportit për stafin " + idStafit, e);
        }
    }

    private List<ArtikujtHumbur> executeQueryList(String sql, Object... params) {
        List<ArtikujtHumbur> list = new ArrayList<>();

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                Object p = params[i];
                if (p instanceof String s) {
                    ps.setString(i + 1, s);
                } else if (p instanceof Integer n) {
                    ps.setInt(i + 1, n);
                } else if (p instanceof Date d) {
                    ps.setDate(i + 1, d);
                } else if (p == null) {
                    ps.setNull(i + 1, Types.NULL);
                } else {
                    ps.setObject(i + 1, p);
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapper.getFromResultSet(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Gabim gjatë executeQueryList()", e);
        }

        return list;
    }
}