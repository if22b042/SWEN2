package com.example.jpademo.service.mapper;

import com.example.jpademo.persistence.entities.TourLogEntity;
import com.example.jpademo.persistence.repositories.TourRepository;
import com.example.jpademo.service.dtos.TourLogDto;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TourLogMapper {

    @Autowired
    private TourRepository tourRepository;

    public TourLogEntity toEntity(TourLogDto tourLogDto) {
        TourLogEntity tourLogEntity = new TourLogEntity();
        tourLogEntity.setId(tourLogDto.getId());
        tourLogEntity.setDateTime(tourLogDto.getDateTime());
        tourLogEntity.setComment(tourLogDto.getComment());
        tourLogEntity.setDifficulty(tourLogDto.getDifficulty());
        tourLogEntity.setTotalDistance(tourLogDto.getTotalDistance());
        tourLogEntity.setTotalTime(tourLogDto.getTotalTime());
        tourLogEntity.setRating(tourLogDto.getRating());
        tourLogEntity.setImagePath(tourLogDto.getImagePath());
        tourLogEntity.setTour(tourRepository.findById(tourLogDto.getTourId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid tour ID: " + tourLogDto.getTourId())));
        return tourLogEntity;
    }

    public TourLogDto toDto(TourLogEntity tourLogEntity) {
        return  TourLogDto.builder()
                .id(new SimpleLongProperty(tourLogEntity.getId()))
                .dateTime(new SimpleObjectProperty<>(tourLogEntity.getDateTime()))
                .comment(new SimpleStringProperty(tourLogEntity.getComment()))
                .difficulty(new SimpleStringProperty(tourLogEntity.getDifficulty()))
                .totalDistance(new SimpleIntegerProperty(tourLogEntity.getTotalDistance()))
                .totalTime(new SimpleIntegerProperty(tourLogEntity.getTotalTime()))
                .rating(new SimpleIntegerProperty(tourLogEntity.getRating()))
                .tourId(new SimpleLongProperty(tourLogEntity.getTour().getId()))
                .imagePath(new SimpleStringProperty(tourLogEntity.getImagePath()))
                .build();
    }

    public void updateEntityFromDto(TourLogDto dto, TourLogEntity entity) {
        if (dto.getDateTime() != null) {
            entity.setDateTime(dto.getDateTime());
        }
        if (dto.getComment() != null) {
            entity.setComment(dto.getComment());
        }
        if (dto.getDifficulty() != null) {
            entity.setDifficulty(dto.getDifficulty());
        }
        if (dto.getTotalDistance() != 0) {
            entity.setTotalDistance(dto.getTotalDistance());
        }
        if (dto.getTotalTime() != 0) {
            entity.setTotalTime(dto.getTotalTime());
        }
        if (dto.getRating() != 0) {
            entity.setRating(dto.getRating());
        }
        if (dto.getImagePath() != null) {
            entity.setImagePath(dto.getImagePath());
        }
    }
}
