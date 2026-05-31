package services;

import app.SessionManager;
import models.Perdoruesi;
import models.dto.BiletaTableDto;
import repository.BiletatRepository;

import java.util.ArrayList;
import java.util.List;

public class BiletatService {
    private final BiletatRepository biletatRepository = new BiletatRepository();

    public List<BiletaTableDto> getMyaTickets() {
        Perdoruesi user = SessionManager.getCurrentUser();

        if (user == null) {
            return new ArrayList<>();
        }

        Integer userId = user.getIdPerdoruesit();
        if (userId == null) {
            return new ArrayList<>();
        }

        return biletatRepository.getBiletatByPerdoruesiId(userId);
    }

    public List<BiletaTableDto> getMyTickets() {
        return getMyaTickets();
    }

    public boolean kryejCheckIn(int idBiletes, String vendiUljes, boolean checkedIn) {
        if (vendiUljes == null || vendiUljes.trim().isEmpty()) {
            return false;
        }
        return biletatRepository.updateCheckIn(idBiletes, vendiUljes.trim().toUpperCase(), checkedIn);
    }
}