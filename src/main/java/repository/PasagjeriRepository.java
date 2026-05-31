package repository;

import models.Pasagjeri;
import models.dto.PasagjeriTableDTO;
import models.mappers.PasagjeriMapper;
import models.mappers.IMapper;
import services.DatabaseService; // E ndryshuar nga service në services sipas klasës tënde

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PasagjeriRepository extends BaseRepository<Pasagjeri> {

    @Override
    protected String tableName() {
        return "pasagjerit";
    }

    @Override
    protected String idColumnName() {
        return "id_pasagjerit";
    }

    @Override
    protected String[] insertColumns() {
        return new String[]{
                "numri_pasaportes", "id_vendit_shtetesise", "emri", "mbiemri",
                "datelindja", "gjinia", "email", "telefoni", "adresa", "pasaporta_skadimi"
        };
    }

    @Override
    protected String[] updateColumns() {
        return new String[]{
                "numri_pasaportes", "id_vendit_shtetesise", "emri", "mbiemri",
                "datelindja", "gjinia", "email", "telefoni", "adresa", "pasaporta_skadimi"
        };
    }

    // Zgjidhja e gabimit të dytë: Kthejmë IMapper<Pasagjeri> sipas rregullit të profesorit
    @Override
    protected IMapper<Pasagjeri> getMapper() {
        return new PasagjeriMapper();
    }

    @Override
    protected void setPstmCreate(PreparedStatement pstm, Pasagjeri obj) throws SQLException {
        pstm.setString(1, obj.getNumriPasaportes());
        pstm.setInt(2, obj.getIdVenditShtetesise());
        pstm.setString(3, obj.getEmri());
        pstm.setString(4, obj.getMbiemri());
        pstm.setDate(5, obj.getDatelindja());
        pstm.setString(6, obj.getGjinia());
        pstm.setString(7, obj.getEmail());
        pstm.setString(8, obj.getTelefoni());
        pstm.setString(9, obj.getAdresa());
        pstm.setDate(10, obj.getPasaportaSkadimiAsDate());
    }

    @Override
    protected void setPstmUpdate(PreparedStatement pstm, Pasagjeri obj) throws SQLException {
        pstm.setString(1, obj.getNumriPasaportes());
        pstm.setInt(2, obj.getIdVenditShtetesise());
        pstm.setString(3, obj.getEmri());
        pstm.setString(4, obj.getMbiemri());
        pstm.setDate(5, obj.getDatelindja());
        pstm.setString(6, obj.getGjinia());
        pstm.setString(7, obj.getEmail());
        pstm.setString(8, obj.getTelefoni());
        pstm.setString(9, obj.getAdresa());
        pstm.setDate(10, obj.getPasaportaSkadimiAsDate());
        pstm.setInt(11, obj.getIdPasagjerit()); // WHERE id_pasagjerit = ?
    }

    // Zgjidhja e gabimit të parë: Override i detyrueshëm i metodës abstrakte delete(T obj)
    @Override
    public boolean delete(Pasagjeri obj) {
        if (obj == null) return false;
        // Thërrasim metodën delete(int id) që profesori e ka shkruar tashmë të gatshme në BaseRepository!
        return this.delete(obj.getIdPasagjerit());
    }

    // --- OPERACIONET STRATEGJIKE BIZNESORE ---

    private static int gjejOseKrijoShtetesineId(String emriShtetit) throws SQLException {
        String shtetiTrimmed = emriShtetit.trim();
        // Përdorim emrat e saktë: id_vendi dhe emri_shqip
        String sqlKerko = "SELECT id_vendi FROM vendet WHERE LOWER(TRIM(emri_shqip)) = LOWER(?)";
        // Kujdes: Tabela 'vendet' ka edhe kolona 'kodi_iso',
        // nëse është NOT NULL duhet ta shtosh edhe atë gjatë INSERT-it.
        String sqlShto = "INSERT INTO vendet (emri_shqip, kodi_iso) VALUES (?, 'XX')";

        try (Connection conn = DatabaseService.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sqlKerko)) {
                stmt.setString(1, shtetiTrimmed);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) return rs.getInt("id_vendi");
                }
            }
            // ... (pjesa tjetër e kodit mbetet e njëjtë)
        }
        throw new SQLException("Dështoi përcaktimi i shtetit.");
    }

    public static boolean shtoPasagjer(Pasagjeri p, String emriShtetit) {
        try {
            int idShtetit = gjejOseKrijoShtetesineId(emriShtetit);
            p.setIdVenditShtetesise(idShtetit);
            PasagjeriRepository repo = new PasagjeriRepository();
            // Thirrja e create(obj) të BaseRepository e cila kthen Pasagjeri
            return repo.create(p) != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean perditesoPasagjer(Pasagjeri p, String emriShtetit) {
        try {
            int idShtetit = gjejOseKrijoShtetesineId(emriShtetit);
            p.setIdVenditShtetesise(idShtetit);
            PasagjeriRepository repo = new PasagjeriRepository();
            // Thirrja e update(obj) të BaseRepository e cila kthen Pasagjeri
            return repo.update(p) != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Metoda statike që përdor kontrollori për të fshirë nga tabela përmes id-së
    public static boolean fshijPasagjer(int id) {
        PasagjeriRepository repo = new PasagjeriRepository();
        return repo.delete(id); // Thërret direkt delete(int id) të gatshme te BaseRepository
    }

    public static List<PasagjeriTableDTO> getAllPasagjeretTable() {
        List<PasagjeriTableDTO> lista = new ArrayList<>();
        // Përdorim emrat e saktë: id_vendi dhe emri_shqip
        String query = "SELECT p.*, v.emri_shqip FROM pasagjerit p " +
                "LEFT JOIN vendet v ON p.id_vendit_shtetesise = v.id_vendi ORDER BY p.id_pasagjerit DESC";

        try (Connection conn = DatabaseService.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                lista.add(new PasagjeriTableDTO(
                        rs.getInt("id_pasagjerit"),
                        rs.getString("numri_pasaportes"),
                        rs.getString("emri"),
                        rs.getString("mbiemri"),
                        rs.getString("emri_shqip") != null ? rs.getString("emri_shqip") : "Pa Përcaktuar",
                        rs.getString("gjinia"),
                        rs.getDate("datelindja") != null ? rs.getDate("datelindja").toLocalDate() : null,
                        rs.getString("email"),
                        rs.getString("telefoni"),
                        rs.getString("adresa"),
                        rs.getDate("pasaporta_skadimi") != null ? rs.getDate("pasaporta_skadimi").toLocalDate() : null
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}