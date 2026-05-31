package services;

import models.dto.AvionetRequestDto;
import models.dto.AvionetTableDto;
import models.dto.LookupDto;
import repository.AvionetRepository;

import java.util.List;

public class AvionetService {
    private final AvionetRepository repository = new AvionetRepository();

    public List<AvionetTableDto> getAllAvionet() {
        return repository.getAll();
    }

    public List<AvionetTableDto> searchAvionet(String query) {
        if (query == null || query.trim().isEmpty()) {
            return repository.getAll();
        }
        return repository.searchByRegister(query);
    }

    public boolean saveAvion(AvionetRequestDto dto) {
        if (dto == null) return false;
        if (dto.getIdKompanise() == null || dto.getIdLlojit() == null) return false;
        if (dto.getNumriRegjistrit() == null || dto.getNumriRegjistrit().trim().isEmpty()) return false;
        return repository.insert(dto);
    }

    public boolean deleteAvion(int idAvionit) {
        return repository.deleteById(idAvionit);
    }

    public List<LookupDto> getKompanite() {
        return repository.getKompaniteLookup();
    }

    public List<LookupDto> getLlojet() {
        return repository.getLlojetLookup();
    }
}