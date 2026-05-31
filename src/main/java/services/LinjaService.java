package services;

import models.Linjat;
import models.dto.*;
import models.mappers.LinjaMapper;
import repository.LinjaRepository;

import java.util.List;
import java.util.stream.Collectors;

public class LinjaService {

    private final LinjaRepository repo   = new LinjaRepository();
    private final LinjaMapper      mapper = new LinjaMapper();

    // ════════════════════════════════════════════════════════════════════
    //  Lexim
    // ════════════════════════════════════════════════════════════════════

    public List<LinjaRowDto> getAll() {
        return repo.getAll().stream().map(this::toRow).collect(Collectors.toList());
    }

    public List<LinjaRowDto> search(String term) {
        if (term == null || term.isBlank()) return getAll();
        return repo.search(term.trim()).stream().map(this::toRow).collect(Collectors.toList());
    }

    public Linjat getById(int id) {
        return repo.getById(id);
    }

    // ════════════════════════════════════════════════════════════════════
    //  Krijo
    // ════════════════════════════════════════════════════════════════════

    public OperacioniResponseDto create(LinjaFormDto dto) {
        OperacioniResponseDto v = validateLinja(dto, 0);
        if (!v.isSuccess()) return v;

        try {
            repo.create(mapper.fromDto(dto));
            return OperacioniResponseDto.ok("Linja u ruajt me sukses.");
        } catch (Exception e) {
            return handleDbError(e);
        }
    }

    // ════════════════════════════════════════════════════════════════════
    //  Përditëso
    // ════════════════════════════════════════════════════════════════════

    public OperacioniResponseDto update(int id, LinjaFormDto dto) {
        OperacioniResponseDto v = validateLinja(dto, id);
        if (!v.isSuccess()) return v;

        try {
            Linjat existing = repo.getById(id);
            if (existing == null)
                return OperacioniResponseDto.error("Linja nuk u gjet.");

            mapper.fromDto(existing, dto);
            repo.update(existing);
            return OperacioniResponseDto.ok("Linja u përditësua me sukses.");
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
                    ? OperacioniResponseDto.ok("Linja u fshi me sukses.")
                    : OperacioniResponseDto.error("Linja nuk u gjet ose ka fluturime aktive.");
        } catch (Exception e) {
            return handleDbError(e);
        }
    }

    // ════════════════════════════════════════════════════════════════════
    //  Validim
    // ════════════════════════════════════════════════════════════════════

    private OperacioniResponseDto validateLinja(LinjaFormDto dto, int excludeId) {
        if (dto.getIdAeroportitNisjes() <= 0)
            return OperacioniResponseDto.error("Zgjidhni aeroportin e nisjes.");
        if (dto.getIdAeroportitMbrrritjes() <= 0)
            return OperacioniResponseDto.error("Zgjidhni aeroportin e mbërritjes.");
        if (dto.getIdAeroportitNisjes() == dto.getIdAeroportitMbrrritjes())
            return OperacioniResponseDto.error("Aeroporti i nisjes dhe mbërritjes nuk mund të jenë të njëjtë.");
        if (repo.existsRoute(dto.getIdAeroportitNisjes(), dto.getIdAeroportitMbrrritjes(), excludeId))
            return OperacioniResponseDto.error("Kjo linjë ekziston tashmë.");
        return OperacioniResponseDto.ok("");
    }

    // ════════════════════════════════════════════════════════════════════
    //  Helpers
    // ════════════════════════════════════════════════════════════════════

    private LinjaRowDto toRow(Linjat l) {
        String nisja = l.getKodiIataNisjes() != null
                ? l.getKodiIataNisjes() + " – " + nvl(l.getEmriNisjes(), "")
                : String.valueOf(l.getIdAeroportitNisjes());
        String mbrrritja = l.getKodiIataMbrrritjes() != null
                ? l.getKodiIataMbrrritjes() + " – " + nvl(l.getEmriMbrrritjes(), "")
                : String.valueOf(l.getIdAeroportitMbrrritjes());

        return new LinjaRowDto(
                l.getIdLinjes(),
                nisja,
                mbrrritja,
                l.getDistancaKm() != null ? l.getDistancaKm() + " km" : "—",
                l.getKohaFluturimitMin() != null ? l.getKohaFluturimitMin() + " min" : "—",
                l.isEshteAktive() ? "Aktive" : "Joaktive"
        );
    }

    private OperacioniResponseDto handleDbError(Exception e) {
        String msg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
        if (msg.contains("duplicate") || msg.contains("unique"))
            return OperacioniResponseDto.error("Kjo linjë ekziston tashmë.");
        if (msg.contains("foreign key") || msg.contains("constraint"))
            return OperacioniResponseDto.error("Nuk mund të fshihet — ka fluturime të lidhura.");
        return OperacioniResponseDto.error("Gabim i brendshëm: " + e.getMessage());
    }

    private String nvl(String v, String fb) {
        return (v != null && !v.isBlank()) ? v : fb;
    }
}