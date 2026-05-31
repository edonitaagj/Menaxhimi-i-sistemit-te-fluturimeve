package services;

import models.KompaniteAjrore;
import models.dto.*;
import models.mappers.KompaniaMapper;
import repository.KompaniaRepository;

import java.util.List;
import java.util.stream.Collectors;

public class KompaniaService {

    private final KompaniaRepository repo   = new KompaniaRepository();
    private final KompaniaMapper      mapper = new KompaniaMapper();

    // ════════════════════════════════════════════════════════════════════
    //  Lexim
    // ════════════════════════════════════════════════════════════════════

    public List<KompaniaRowDto> getAll() {
        return repo.getAll().stream().map(this::toRow).collect(Collectors.toList());
    }

    public List<KompaniaRowDto> search(String term) {
        if (term == null || term.isBlank()) return getAll();
        return repo.search(term.trim()).stream().map(this::toRow).collect(Collectors.toList());
    }

    /** Kthe modelin e plotë — për ta ngarkuar në formë pas klikimit në tabelë. */
    public KompaniteAjrore getById(int id) {
        return repo.getById(id);
    }

    // ════════════════════════════════════════════════════════════════════
    //  Krijo
    // ════════════════════════════════════════════════════════════════════

    public OperacioniResponseDto create(KompaniaFormDto dto) {
        // Validim
        OperacioniResponseDto v = validateKompania(dto, 0);
        if (!v.isSuccess()) return v;

        try {
            KompaniteAjrore k = mapper.fromDto(dto);
            repo.create(k);
            return OperacioniResponseDto.ok("Kompania u ruajt me sukses.");
        } catch (Exception e) {
            return handleDbError(e);
        }
    }

    // ════════════════════════════════════════════════════════════════════
    //  Përditëso
    // ════════════════════════════════════════════════════════════════════

    public OperacioniResponseDto update(int id, KompaniaFormDto dto) {
        OperacioniResponseDto v = validateKompania(dto, id);
        if (!v.isSuccess()) return v;

        try {
            KompaniteAjrore existing = repo.getById(id);
            if (existing == null)
                return OperacioniResponseDto.error("Kompania nuk u gjet.");

            mapper.fromDto(existing, dto);  // aplikon ndryshimet in-place
            repo.update(existing);
            return OperacioniResponseDto.ok("Kompania u përditësua me sukses.");
        } catch (Exception e) {
            return handleDbError(e);
        }
    }

    // ════════════════════════════════════════════════════════════════════
    //  Fshij
    // ════════════════════════════════════════════════════════════════════

    public OperacioniResponseDto delete(int id) {
        try {
            boolean deleted = repo.delete(id);
            return deleted
                    ? OperacioniResponseDto.ok("Kompania u fshi me sukses.")
                    : OperacioniResponseDto.error("Kompania nuk u gjet ose ka fluturime aktive.");
        } catch (Exception e) {
            return handleDbError(e);
        }
    }

    // ════════════════════════════════════════════════════════════════════
    //  Validim i centralizuar
    // ════════════════════════════════════════════════════════════════════

    private OperacioniResponseDto validateKompania(KompaniaFormDto dto, int excludeId) {
        if (dto.getEmri().isEmpty())
            return OperacioniResponseDto.error("Emri i kompanisë është i detyrueshëm.");
        if (dto.getKodiIata().isEmpty() || dto.getKodiIata().length() != 2)
            return OperacioniResponseDto.error("Kodi IATA duhet të jetë saktësisht 2 karaktere.");
        if (dto.getKodiIcao().isEmpty() || dto.getKodiIcao().length() != 3)
            return OperacioniResponseDto.error("Kodi ICAO duhet të jetë saktësisht 3 karaktere.");
        if (dto.getIdVendit() <= 0)
            return OperacioniResponseDto.error("Zgjidhni shtetin e origjinës.");
        if (repo.existsIata(dto.getKodiIata(), excludeId))
            return OperacioniResponseDto.error("Kodi IATA '" + dto.getKodiIata() + "' ekziston tashmë.");
        return OperacioniResponseDto.ok("");
    }

    // ════════════════════════════════════════════════════════════════════
    //  Helpers
    // ════════════════════════════════════════════════════════════════════

    private KompaniaRowDto toRow(KompaniteAjrore k) {
        return new KompaniaRowDto(
                k.getIdKompanise(),
                nvl(k.getEmri(),         "—"),
                nvl(k.getKodiIata(),     "—"),
                nvl(k.getKodiIcao(),     "—"),
                nvl(k.getEmriIShkurter(),   "—"),
                nvl(k.getFaqjaWeb(),     "—"),
                k.getEshteAktive() ? "Aktive" : "Joaktive"
        );
    }

    private OperacioniResponseDto handleDbError(Exception e) {
        String msg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
        if (msg.contains("duplicate") || msg.contains("unique"))
            return OperacioniResponseDto.error("Kodi IATA ose ICAO ekziston tashmë.");
        if (msg.contains("foreign key") || msg.contains("constraint"))
            return OperacioniResponseDto.error("Nuk mund të fshihet — ka fluturime ose avionë të lidhur.");
        return OperacioniResponseDto.error("Gabim i brendshëm: " + e.getMessage());
    }

    private String nvl(String v, String fb) {
        return (v != null && !v.isBlank()) ? v : fb;
    }
}