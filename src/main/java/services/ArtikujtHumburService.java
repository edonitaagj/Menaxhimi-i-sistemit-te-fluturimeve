package services;

import models.ArtikujtHumbur;
import models.dto.ArtikullHumburRequestDto;
import models.mappers.ArtikullHumburMapper;
import repository.ArtikujtHumburRepository;

import java.util.List;

public class ArtikujtHumburService {

    private final ArtikujtHumburRepository repo =
            new ArtikujtHumburRepository();

    private final ArtikullHumburMapper mapper =
            new ArtikullHumburMapper();

    public ArtikujtHumbur save(ArtikullHumburRequestDto dto) {

        if(dto == null){
            throw new IllegalArgumentException("DTO është null.");
        }

        if(dto.getArtikulli() == null || dto.getArtikulli().isBlank()){
            throw new IllegalArgumentException("Artikulli është i detyrueshëm.");
        }

        if(dto.getVendi() == null || dto.getVendi().isBlank()){
            throw new IllegalArgumentException("Vendi është i detyrueshëm.");
        }

        if(dto.getDataGjetjes() == null || dto.getDataGjetjes().isBlank()){
            throw new IllegalArgumentException("Data është e detyrueshme.");
        }

        ArtikujtHumbur entity = mapper.fromDto(dto);

        /*
         * Vlerat default
         */

        entity.setIdAeroportit(1);

        entity.setIdStafitRaportues(1);

        return repo.create(entity);
    }

    public List<ArtikujtHumbur> getAll() {
        return repo.findAll();
    }

    public List<ArtikujtHumbur> search(String keyword) {

        if(keyword == null || keyword.isBlank()){
            return getAll();
        }

        return repo.search(keyword);
    }
}