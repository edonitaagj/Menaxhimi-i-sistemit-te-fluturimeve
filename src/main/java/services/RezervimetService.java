package services;

import app.SessionManager;
import models.Perdoruesi;
import models.dto.FluturimSelectDto;
import models.dto.OperationResponseDto;
import models.dto.RezervimiCreateRequestDto;
import models.dto.RezervimiCreateResponseDto;
import models.dto.RezervimiTableDto;
import repository.RezervimetRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

public class RezervimetService {

    private final RezervimetRepository rezervimetRepository = new RezervimetRepository();

    public List<FluturimSelectDto> getAvailableFlights() {
        return rezervimetRepository.findAvailableFlights();
    }

    public RezervimiCreateResponseDto createReservation(FluturimSelectDto fluturimi, String klasa, String shenimet) {
        Perdoruesi currentUser = SessionManager.getCurrentUser();
        if (currentUser == null || currentUser.getEmail() == null || currentUser.getEmail().isBlank()) {
            return new RezervimiCreateResponseDto(false, "User-i nuk është i loguar.");
        }

        Integer idPasagjerit = rezervimetRepository.findPassengerIdByEmail(currentUser.getEmail());
        if (idPasagjerit == null) {
            return new RezervimiCreateResponseDto(false, "Nuk u gjet pasagjeri i lidhur me këtë email.");
        }

        if (fluturimi == null) {
            return new RezervimiCreateResponseDto(false, "Duhet të zgjidhni një fluturim.");
        }

        if (rezervimetRepository.existsReservationForPassengerOnFlight(idPasagjerit, fluturimi.getIdFluturimit())) {
            return new RezervimiCreateResponseDto(false, "Ju tashmë keni rezervim për këtë fluturim.");
        }

        BigDecimal cmimi = calculatePrice(fluturimi.getDistancaKm(), klasa);
        RezervimiCreateRequestDto request = new RezervimiCreateRequestDto(
                fluturimi.getIdFluturimit(),
                klasa,
                cmimi,
                "EUR",
                shenimet
        );

        RezervimiCreateResponseDto response = rezervimetRepository.createReservation(idPasagjerit, request);

        if (response.isSuccess()) {
            SessionManager.setPendingReservationId(response.getIdRezervimit());
            SessionManager.setPendingReservationCode(response.getKodiRezervimit());
        }

        return response;
    }

    public List<RezervimiTableDto> getMyRezervimet() {
        Perdoruesi currentUser = SessionManager.getCurrentUser();
        if (currentUser == null || currentUser.getEmail() == null || currentUser.getEmail().isBlank()) {
            return Collections.emptyList();
        }

        Integer idPasagjerit = rezervimetRepository.findPassengerIdByEmail(currentUser.getEmail());
        if (idPasagjerit == null) {
            return Collections.emptyList();
        }

        return rezervimetRepository.findReservationsByPassenger(idPasagjerit);
    }

    public OperationResponseDto cancelMyReservation(int idRezervimit) {
        Perdoruesi currentUser = SessionManager.getCurrentUser();
        if (currentUser == null || currentUser.getEmail() == null || currentUser.getEmail().isBlank()) {
            return new OperationResponseDto(false, "User-i nuk është i loguar.");
        }

        Integer idPasagjerit = rezervimetRepository.findPassengerIdByEmail(currentUser.getEmail());
        if (idPasagjerit == null) {
            return new OperationResponseDto(false, "Pasagjeri nuk u gjet.");
        }

        return rezervimetRepository.cancelReservation(idRezervimit, idPasagjerit);
    }

    private BigDecimal calculatePrice(Integer distancaKm, String klasa) {
        BigDecimal base;

        if (distancaKm == null || distancaKm <= 0) {
            base = new BigDecimal("35.00");
        } else {
            base = new BigDecimal(distancaKm)
                    .multiply(new BigDecimal("0.08"))
                    .setScale(2, RoundingMode.HALF_UP);

            if (base.compareTo(new BigDecimal("35.00")) < 0) {
                base = new BigDecimal("35.00");
            }
        }

        BigDecimal multiplier = switch (klasa == null ? "" : klasa.toLowerCase()) {
            case "biznes" -> new BigDecimal("1.60");
            case "first" -> new BigDecimal("2.30");
            default -> new BigDecimal("1.00");
        };

        return base.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
    }
}