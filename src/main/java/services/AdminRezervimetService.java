package services;

import models.dto.RezervimeTableDto;
import repository.AdminRezervimetRepository;

import java.util.List;

public class AdminRezervimetService {
    private final AdminRezervimetRepository repository = new AdminRezervimetRepository();

    public List<RezervimeTableDto> getAllRezervimet() {
        return repository.getAll();
    }

    public List<RezervimeTableDto> searchRezervimet(String query) {
        if (query == null || query.trim().isEmpty()) {
            return repository.getAll();
        }
        return repository.search(query);
    }

    public int getTodayReservationsCount() {
        return repository.countTodayReservations();
    }

    public double getTodayRevenue() {
        return repository.sumTodayRevenue();
    }
}