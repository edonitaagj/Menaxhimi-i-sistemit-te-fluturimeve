package services;

import models.Fluturimet;
import models.dto.AdminFluturimiFormDto;
import models.dto.AdminFluturimiRowDto;
import models.dto.KompaniaItem;
import models.dto.OperacioniResponseDto;
import models.mappers.AdminFluturimiMapper;
import repository.AdminFluturimiRepository;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class AdminFluturimiService {

    private final AdminFluturimiRepository repo   = new AdminFluturimiRepository();
    private final AdminFluturimiMapper     mapper = new AdminFluturimiMapper();
    private final KompaniaItemService      kompService = new KompaniaItemService();

    private static final DateTimeFormatter HH_MM = DateTimeFormatter.ofPattern("HH:mm");

    // ════════════════════════════════════════════════════════════════════
    //  Lexim
    // ════════════════════════════════════════════════════════════════════

    public List<AdminFluturimiRowDto> getAll() {
        return repo.getAll().stream().map(this::toRow).collect(Collectors.toList());
    }

    public List<AdminFluturimiRowDto> search(String term) {
        if (term == null || term.isBlank()) return getAll();
        return repo.search(term.trim()).stream().map(this::toRow).collect(Collectors.toList());
    }

    /** Kthe KompaniaItem-et për cmbKompania. */
    public List<KompaniaItem> getKompaniteItems() {
        return kompService.getAllAsItems();
    }

    /** Statuset e mundshme për cmbStatusi. */
    public List<String> getStatusetItems() {
        return List.of(
                "i_planifikuar", "boarding", "ngritur",
                "ne_fluturim", "zbritur", "mberriti",
                "anuluar", "i_vonuar", "devijuar"
        );
    }

    // ════════════════════════════════════════════════════════════════════
    //  Krijo fluturim të ri
    // ════════════════════════════════════════════════════════════════════

    public OperacioniResponseDto create(AdminFluturimiFormDto dto) {
        // ── Validim ──────────────────────────────────────────────────────
        if (dto.getKodi().isEmpty())
            return OperacioniResponseDto.error("Kodi i fluturimit është i detyrueshëm.");
        if (dto.getIdKompanise() <= 0)
            return OperacioniResponseDto.error("Zgjidhni kompaninë ajrore.");
        if (dto.getIataOrigjina().isEmpty() || dto.getIataOrigjina().length() != 3)
            return OperacioniResponseDto.error("Origjina duhet të jetë kodi IATA 3-shkronjësh (p.sh. PRN).");
        if (dto.getIataDestinacioni().isEmpty() || dto.getIataDestinacioni().length() != 3)
            return OperacioniResponseDto.error("Destinacioni duhet të jetë kodi IATA 3-shkronjësh (p.sh. MUC).");
        if (dto.getIataOrigjina().equals(dto.getIataDestinacioni()))
            return OperacioniResponseDto.error("Origjina dhe destinacioni nuk mund të jenë të njëjta.");

        // ── Gjej linjën nga IATA ─────────────────────────────────────────
        Integer idLinjes = repo.findLinjaId(dto.getIataOrigjina(), dto.getIataDestinacioni());
        if (idLinjes == null)
            return OperacioniResponseDto.error(
                    "Linja " + dto.getIataOrigjina() + " → " + dto.getIataDestinacioni() +
                            " nuk ekziston. Shtojeni te Kompanitë & Linjat.");

        // ── Gjej avionin default ─────────────────────────────────────────
        Integer idAvionit = repo.findDefaultAvioni(dto.getIdKompanise());
        if (idAvionit == null)
            return OperacioniResponseDto.error(
                    "Kompania nuk ka avionë aktivë. Shtoni avion te Avionët & Mirëmbajtja.");

        // ── Krijo objektin dhe INSERT ────────────────────────────────────
        try {
            Fluturimet f = mapper.fromDto(dto);
            f.setIdLinjes(idLinjes);
            f.setIdAvionit(idAvionit);
            repo.create(f);
            return OperacioniResponseDto.ok("Fluturimi u regjistrua me sukses.");
        } catch (Exception e) {
            return handleDbError(e);
        }
    }

    // ════════════════════════════════════════════════════════════════════
    //  Ndrysho statusin (nga butoni i kolumës VEPRIMET)
    // ════════════════════════════════════════════════════════════════════

    public OperacioniResponseDto updateStatusi(int idFluturimit, String statusiRi) {
        if (statusiRi == null || statusiRi.isBlank())
            return OperacioniResponseDto.error("Statusi nuk mund të jetë bosh.");
        try {
            repo.updateStatusi(idFluturimit, statusiRi);
            return OperacioniResponseDto.ok("Statusi u ndryshua.");
        } catch (Exception e) {
            return handleDbError(e);
        }
    }

    // ════════════════════════════════════════════════════════════════════
    //  Fshij
    // ════════════════════════════════════════════════════════════════════

    public OperacioniResponseDto delete(int idFluturimit) {
        try {
            boolean deleted = repo.delete(idFluturimit);
            return deleted
                    ? OperacioniResponseDto.ok("Fluturimi u fshi me sukses.")
                    : OperacioniResponseDto.error("Fluturimi nuk u gjet.");
        } catch (Exception e) {
            return handleDbError(e);
        }
    }

    // ════════════════════════════════════════════════════════════════════
    //  Helpers private
    // ════════════════════════════════════════════════════════════════════

    private AdminFluturimiRowDto toRow(Fluturimet f) {
        return new AdminFluturimiRowDto(
                f.getIdFluturimit(),
                nvl(f.getNumriFluturimit(),      "—"),
                nvl(f.getEmriKompanise(),         "—"),
                nvl(f.getKodiIataNisjes(),        "—"),
                nvl(f.getKodiIataDestinacioni(),  "—"),
                nvl(f.getKodiGejtiNisjes(),       "—"),
                nvl(f.getStatusi(),               "—")
        );
    }

    private OperacioniResponseDto handleDbError(Exception e) {
        String msg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
        if (msg.contains("duplicate") || msg.contains("unique"))
            return OperacioniResponseDto.error("Kodi i fluturimit ekziston tashmë.");
        if (msg.contains("foreign key") || msg.contains("constraint"))
            return OperacioniResponseDto.error("Nuk mund të fshihet — ka rezervime ose bileta të lidhura.");
        return OperacioniResponseDto.error("Gabim i brendshëm: " + e.getMessage());
    }

    private String nvl(String v, String fb) {
        return (v != null && !v.isBlank()) ? v : fb;
    }
}