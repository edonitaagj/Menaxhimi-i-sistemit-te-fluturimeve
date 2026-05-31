package services;

import app.SessionManager;
import models.Perdoruesi;
import models.dto.FjalekalimiUpdateDto;
import models.dto.ProfilResponseDto;
import models.dto.ProfilUpdateDto;
import models.dto.ProfilViewDto;
import repository.ProfilRepository;
import repository.UserRepository;

public class ProfilService {

    // ProfilRepository — për UPDATE specifike të profilit
    private final ProfilRepository profilRepo = new ProfilRepository();

    // UserRepository — vetëm për getById() pas UPDATE (rifreskim sesioni)
    private final UserRepository   userRepo   = new UserRepository();

    // ════════════════════════════════════════════════════════════════════
    //  Lexo profilin — PA query DB, merr nga SessionManager
    // ════════════════════════════════════════════════════════════════════
    public ProfilViewDto getProfilForView() {
        Perdoruesi user = SessionManager.getCurrentUser();
        if (user == null) return null;

        return new ProfilViewDto(
                nvl(user.getFullName(), ""),
                nvl(user.getUsername(), ""),
                nvl(user.getEmail(),    ""),
                nvl(user.getRoli(),     "klient")
        );
    }

    // ════════════════════════════════════════════════════════════════════
    //  Përditëso emrin + mbiemrin
    // ════════════════════════════════════════════════════════════════════
    public ProfilResponseDto updateProfil(ProfilUpdateDto dto) {

        Perdoruesi user = SessionManager.getCurrentUser();

        if (user == null)
            return ProfilResponseDto.error(
                    "Sesioni ka skaduar. Hyni sërish."
            );

        String emriPlote = dto.getEmriPlote();
        String email     = dto.getEmail();

        if (emriPlote.isEmpty())
            return ProfilResponseDto.error(
                    "Emri i plotë nuk mund të jetë bosh."
            );

        if (email.isEmpty())
            return ProfilResponseDto.error(
                    "Email nuk mund të jetë bosh."
            );

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            return ProfilResponseDto.error(
                    "Email nuk është valid."
            );

        String[] pjeset = emriPlote.split(" ", 2);

        String emriRi =
                pjeset[0].trim();

        String mbiemriRi =
                pjeset.length > 1
                        ? pjeset[1].trim()
                        : "";

        if (emriRi.isEmpty())
            return ProfilResponseDto.error(
                    "Emri nuk mund të jetë bosh."
            );

        if (mbiemriRi.isEmpty())
            return ProfilResponseDto.error(
                    "Shkruaj emrin dhe mbiemrin."
            );

        try {

            profilRepo.updateProfil(
                    user.getIdPerdoruesit(),
                    emriRi,
                    mbiemriRi,
                    email
            );

            SessionManager.login(
                    userRepo.getById(user.getIdPerdoruesit())
            );

            return ProfilResponseDto.ok(
                    "Profili u përditësua me sukses."
            );

        } catch (Exception e) {

            return ProfilResponseDto.error(
                    "Gabim gjatë ruajtjes: "
                            + e.getMessage()
            );
        }
    }
    // ════════════════════════════════════════════════════════════════════
    //  Ndrysho fjalëkalimin
    // ════════════════════════════════════════════════════════════════════
    public ProfilResponseDto updateFjalekalim(FjalekalimiUpdateDto dto) {
        Perdoruesi user = SessionManager.getCurrentUser();
        if (user == null)
            return ProfilResponseDto.error("Sesioni ka skaduar. Hyni sërish.");

        if (dto.getFjalekalimiAktual().isEmpty() || dto.getFjalekalimiRi().isEmpty())
            return ProfilResponseDto.error("Plotësoni të dy fushat e fjalëkalimit.");

        if (dto.getFjalekalimiRi().length() < 8)
            return ProfilResponseDto.error("Fjalëkalimi i ri duhet të ketë të paktën 8 karaktere.");

        if (dto.getFjalekalimiAktual().equals(dto.getFjalekalimiRi()))
            return ProfilResponseDto.error("Fjalëkalimi i ri duhet të jetë i ndryshëm nga ai aktual.");

        if (!HashService.validatePassword(dto.getFjalekalimiAktual(), user.getPasswordHash()))
            return ProfilResponseDto.error("Fjalëkalimi aktual është i gabuar.");

        try {
            String hashiRi = HashService.generateHash(dto.getFjalekalimiRi());
            profilRepo.updatePassword(user.getIdPerdoruesit(), hashiRi);
            // Pa query shtesë — vetëm update in-memory
            user.setPasswordHash(hashiRi);
            SessionManager.login(user);
            return ProfilResponseDto.ok("Fjalëkalimi u ndryshua me sukses.");
        } catch (Exception e) {
            return ProfilResponseDto.error("Gabim gjatë ndryshimit: " + e.getMessage());
        }
    }

    private String nvl(String v, String fallback) {
        return (v != null && !v.isBlank()) ? v : fallback;
    }
}