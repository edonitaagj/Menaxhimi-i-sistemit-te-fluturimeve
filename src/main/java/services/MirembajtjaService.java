package services;

import models.dto.LookupDto;
import models.dto.MirembajtjaRequestDto;
import models.dto.MirembajtjaTableDto;
import models.dto.StaffLookupDto;
import repository.MirembajtjaRepository;

import java.util.List;

public class MirembajtjaService {
    private final MirembajtjaRepository repository = new MirembajtjaRepository();

    public List<MirembajtjaTableDto> getByAvionId(int idAvionit) {
        return repository.getByAvionId(idAvionit);
    }

    public boolean saveMirembajtje(MirembajtjaRequestDto dto) {
        if (dto == null) return false;
        if (dto.getIdAvionit() == null || dto.getIdStafitPergjegjes() == null) return false;
        if (dto.getLlojiSherbimit() == null || dto.getLlojiSherbimit().trim().isEmpty()) return false;
        if (dto.getDataFillimit() == null) return false;
        if (dto.getKostoja() != null && dto.getKostoja() < 0) return false;
        return repository.insert(dto);
    }

    public List<StaffLookupDto> getStafi() {
        return repository.getStafiLookup();
    }

    public List<LookupDto> getLlojetSherbimit() {
        return repository.getLlojetSherbimit();
    }

    public List<LookupDto> getStatusetMirembajtjes() {
        return repository.getStatusetMirembajtjes();
    }
}