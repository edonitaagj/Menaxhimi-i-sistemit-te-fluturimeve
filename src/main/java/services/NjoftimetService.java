package services;

import models.dto.NjoftimiTableDto;
import repository.NjoftimetRepository;

import java.util.List;

public class NjoftimetService {

    private final NjoftimetRepository njoftimetRepository = new NjoftimetRepository();

    public List<NjoftimiTableDto> getAllNotifications() {
        return njoftimetRepository.findAllActiveNotifications();
    }
}