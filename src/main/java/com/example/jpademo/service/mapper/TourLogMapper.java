package com.example.jpademo.service.mapper;

import com.example.jpademo.persistence.entities.TourEntity;
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
        TourEntity tour = tourRepository.findById(tourLogDto.getTourId()).orElseThrow(() ->
                new IllegalArgumentException("Invalid tour ID: " + tourLogDto.getTourId()));

        return TourLogEntity.builder()
                .id(tourLogDto.getId())
                .dateTime(tourLogDto.getDateTime())
                .comment(tourLogDto.getComment())
                .difficulty(tourLogDto.getDifficulty())
                .totalDistance(tourLogDto.getTotalDistance())
                .totalTime(tourLogDto.getTotalTime())
                .rating(tourLogDto.getRating())
                .tour(tour)
                .build();
    }

    public TourLogDto toDto(TourLogEntity tourLogEntity) {
        return TourLogDto.builder()
                .id(new SimpleLongProperty(tourLogEntity.getId()))
                .dateTime(new SimpleObjectProperty<>(tourLogEntity.getDateTime()))
                .comment(new SimpleStringProperty(tourLogEntity.getComment()))
                .difficulty(new SimpleStringProperty(tourLogEntity.getDifficulty()))
                .totalDistance(new SimpleIntegerProperty(tourLogEntity.getTotalDistance()))
                .totalTime(new SimpleIntegerProperty(tourLogEntity.getTotalTime()))
                .rating(new SimpleIntegerProperty(tourLogEntity.getRating()))
                .tourId(new SimpleLongProperty(tourLogEntity.getTour().getId()))
                .build();
    }
}
