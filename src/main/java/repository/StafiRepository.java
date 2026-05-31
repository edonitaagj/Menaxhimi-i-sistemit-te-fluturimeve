package repository;

import models.Stafi;
import models.dto.StafiTableDTO;
import models.dto.StafiStatsDTO;
import models.mappers.IMapper;
import models.mappers.StafiMapper;
import services.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class StafiRepository extends BaseRepository<Stafi> {

    private final IMapper<Stafi> mapper = new StafiMapper();

    // =========================================================================
    // 1. IMPLEMENTIMI I METODAVE ABSTRAKTE NGA BASE REPOSITORY (Sipas profës)
    // =========================================================================

    @Override
    protected String tableName() {
        return "stafi";
    }

    @Override
    protected String idColumnName() {
        return "id_stafit";
    }

    @Override
    protected String[] insertColumns() {
        return new String[] {
                "id_rolit", "id_kompanise", "id_aeroportit", "emri", "mbiemri",
                "numri_punonjesit", "email_punes", "telefoni", "data_fillimit", "eshte_aktiv"
        };
    }

    @Override
    protected String[] updateColumns() {
        // Këtu kemi saktësisht 7 kolona (Kemi hequr "paga" sepse nuk ekziston në DB)
        return new String[] {
                "id_rolit",       // ? (Parametri 1)
                "emri",           // ? (Parametri 2)
                "mbiemri",        // ? (Parametri 3)
                "email_punes",    // ? (Parametri 4)
                "telefoni",       // ? (Parametri 5)
                "eshte_aktiv",    // ? (Parametri 6)
                "data_mbarimit"   // ? (Parametri 7)
        };
    }

    @Override
    protected IMapper<Stafi> getMapper() {
        return this.mapper;
    }

    @Override
    protected void setPstmCreate(java.sql.PreparedStatement pstm, models.Stafi obj) throws java.sql.SQLException {
        pstm.setInt(1, obj.getIdRolit());

        if (obj.getIdKompanise() == null) pstm.setNull(2, java.sql.Types.INTEGER);
        else pstm.setInt(2, obj.getIdKompanise());

        if (obj.getIdAeroportit() == null) pstm.setNull(3, java.sql.Types.INTEGER);
        else pstm.setInt(3, obj.getIdAeroportit());

        pstm.setString(4, obj.getEmri());
        pstm.setString(5, obj.getMbiemri());
        pstm.setString(6, obj.getNumriPunonjesit());
        pstm.setString(7, obj.getEmailPunes());
        pstm.setString(8, obj.getTelefoni());
        pstm.setDate(9, obj.getDataFillimit());
        pstm.setInt(10, obj.getEshteAktiv() ? 1 : 0);
    }

    @Override
    protected void setPstmUpdate(java.sql.PreparedStatement pstm, models.Stafi obj) throws java.sql.SQLException {
        pstm.setInt(1, obj.getIdRolit());
        pstm.setString(2, obj.getEmri());
        pstm.setString(3, obj.getMbiemri());
        pstm.setString(4, obj.getEmailPunes());
        pstm.setString(5, obj.getTelefoni());
        pstm.setInt(6, obj.getEshteAktiv() ? 1 : 0);
        pstm.setDate(7, obj.getDataMbarimit());
        pstm.setInt(8, obj.getIdStafit()); // WHERE id_stafit = ?
    }

    @Override
    public boolean delete(Stafi obj) {
        if (obj == null) return false;
        return delete(obj.getIdStafit()); // Thërret metodën delete(int id) të BaseRepository
    }

    // =========================================================================
    // 2. METODAT E SHTIMIT DHE EDITIMIT (Përdorin create/update të profës)
    // =========================================================================

    public static boolean shtoPunonjesTeRi(Stafi punonjes, String emriRoli, String departamenti) {
        try {
            // Gjejmë ose krijojmë rolin fillimisht
            int idRolit = gjejOseKrijoRolinId(emriRoli, departamenti);
            punonjes.setIdRolit(idRolit);

            // Thërrasim metodën create() nga BaseRepository në mënyrë të rregullt
            StafiRepository repo = new StafiRepository();
            repo.create(punonjes);
            return true;
        } catch (Exception e) {
            System.err.println("Gabim gjatë shtimit të punonjësit përmes BaseRepository: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean perditesoPunonjes(Stafi punonjes, String emriRoli, String departamenti) {
        try {
            // Kontrollojmë rolin e ri ose ekzistues
            int idRolit = gjejOseKrijoRolinId(emriRoli, departamenti);
            punonjes.setIdRolit(idRolit);

            // Thërrasim metodën update() nga BaseRepository
            StafiRepository repo = new StafiRepository();
            repo.update(punonjes);
            return true;
        } catch (Exception e) {
            System.err.println("Gabim gjatë përditësimit të punonjësit përmes BaseRepository: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Ndihmës i përbashkët për menaxhimin e rolit përpara manipulimit të stafit
    // Ndihmës i përbashkët për menaxhimin e rolit përpara manipulimit të stafit
    private static int gjejOseKrijoRolinId(String emriRoli, String departamenti) throws SQLException {
        // Pastrojmë hapësirat e tepërta në fillim dhe fund
        String emriRoliTrimmed = emriRoli.trim();
        String departamentiTrimmed = departamenti.trim();

        String sqlKerkoRoli = "SELECT id_rolit FROM roli_stafit WHERE LOWER(TRIM(emri_roli)) = LOWER(?) AND LOWER(TRIM(departamenti)) = LOWER(?)";
        String sqlShtoRoli = "INSERT INTO roli_stafit (emri_roli, departamenti) VALUES (?, ?)";

        // Përdorim bllokun try-with-resources për të siguruar mbylljen e saktë të lidhjeve
        try (Connection conn = DatabaseService.getConnection()) {

            // Hapi 1: Kontrollojmë nëse ekziston roli ekzaktësisht
            try (PreparedStatement stmtKerko = conn.prepareStatement(sqlKerkoRoli)) {
                stmtKerko.setString(1, emriRoliTrimmed);
                stmtKerko.setString(2, departamentiTrimmed);
                try (ResultSet rs = stmtKerko.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("id_rolit");
                    }
                }
            }

            // Hapi 2: Nëse nuk u gjet, tentojmë ta krijojmë të riun
            try (PreparedStatement stmtShto = conn.prepareStatement(sqlShtoRoli, Statement.RETURN_GENERATED_KEYS)) {
                stmtShto.setString(1, emriRoliTrimmed);
                stmtShto.setString(2, departamentiTrimmed);
                stmtShto.executeUpdate();

                try (ResultSet gk = stmtShto.getGeneratedKeys()) {
                    if (gk.next()) {
                        return gk.getInt(1);
                    }
                }
            } catch (SQLException e) {
                // Mbrojtja (Fallback): Nëse ndodh "Duplicate entry" gjatë insertimit (pasi u shpërndava nga kushtet e mësipërme),
                // bëjmë një kërkim të fundit për të marrë ID-në e rolit që sapo na bllokoi, në vend që të kthejmë error.
                try (PreparedStatement stmtRikerko = conn.prepareStatement(sqlKerkoRoli)) {
                    stmtRikerko.setString(1, emriRoliTrimmed);
                    stmtRikerko.setString(2, departamentiTrimmed);
                    try (ResultSet rs = stmtRikerko.executeQuery()) {
                        if (rs.next()) {
                            return rs.getInt("id_rolit");
                        }
                    }
                }
                // Nëse përsëri dështon për ndonjë arsye tjetër, e hedhim gabimin origjinal
                throw e;
            }
        }
        throw new SQLException("Dështoi përcaktimi ose krijimi i rolit për departamentin: " + departamentiTrimmed);
    }

    // =========================================================================
    // 3. LOGJIKA E GRAFIKUT/KARTAVE EKZISTUESE DHE TABELËS JAVAFX
    // =========================================================================

    public static StafiStatsDTO getStaffStatistics() {
        int totali = 0;
        int aktiv = 0;
        int departamente = 0;

        String queryTotali = "SELECT COUNT(*) FROM stafi";
        String queryAktiv = "SELECT COUNT(*) FROM stafi WHERE eshte_aktiv = 1";
        String queryDept = "SELECT COUNT(DISTINCT departamenti) FROM roli_stafit";

        try (Connection conn = DatabaseService.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(queryTotali); ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) totali = rs.getInt(1);
            }
            try (PreparedStatement stmt = conn.prepareStatement(queryAktiv); ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) aktiv = rs.getInt(1);
            }
            try (PreparedStatement stmt = conn.prepareStatement(queryDept); ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) departamente = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Gabim gjatë leximit të statistikave të stafit: " + e.getMessage());
        }

        return new StafiStatsDTO(totali, aktiv, departamente);
    }

    public static ObservableList<StafiTableDTO> getAllStaffForTable() {
        ObservableList<StafiTableDTO> lista = FXCollections.observableArrayList();

        String query = "SELECT s.id_stafit, s.emri, s.mbiemri, r.emri_roli, r.departamenti, s.email_punes, s.eshte_aktiv " +
                "FROM stafi s " +
                "INNER JOIN roli_stafit r ON s.id_rolit = r.id_rolit";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new StafiTableDTO(
                        rs.getInt("id_stafit"),
                        rs.getString("emri"),
                        rs.getString("mbiemri"),
                        rs.getString("emri_roli"),
                        rs.getString("departamenti"),
                        rs.getString("email_punes"),
                        rs.getBoolean("eshte_aktiv")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Gabim gjatë leximit të stafit për tabelë: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }
}