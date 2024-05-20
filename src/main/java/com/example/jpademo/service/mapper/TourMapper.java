package com.example.jpademo.service.mapper;

import com.example.jpademo.persistence.entities.TourEntity;
import com.example.jpademo.service.dtos.TourDto;
import org.springframework.stereotype.Component;

@Component
public class TourMapper {

    public TourEntity toEntity(TourDto tourDto) {
        return TourEntity.builder()
                .id(tourDto.getId())
                .title(tourDto.getTitle())
                .description(tourDto.getDescription())
                .startLocation(tourDto.getStartLocation())
                .endLocation(tourDto.getEndLocation())
                .transportation(tourDto.getTransportation())
                .distance(tourDto.getDistance())
                .time(tourDto.getTime())
                .information(tourDto.getInformation())
                .build();
    }

    public TourDto toDto(TourEntity tourEntity) {
        return TourDto.builder()
                .id(tourEntity.getId())
                .title(tourEntity.getTitle())
                .description(tourEntity.getDescription())
                .startLocation(tourEntity.getStartLocation())
                .endLocation(tourEntity.getEndLocation())
                .transportation(tourEntity.getTransportation())
                .distance(tourEntity.getDistance())
                .time(tourEntity.getTime())
                .information(tourEntity.getInformation())
                .build();
    }

    public void updateEntityFromDto(TourDto tourDto, TourEntity tourEntity) {
        tourEntity.setTitle(tourDto.getTitle());
        tourEntity.setDescription(tourDto.getDescription());
        tourEntity.setStartLocation(tourDto.getStartLocation());
        tourEntity.setEndLocation(tourDto.getEndLocation());
        tourEntity.setTransportation(tourDto.getTransportation());
        tourEntity.setDistance(tourDto.getDistance());
        tourEntity.setTime(tourDto.getTime());
        tourEntity.setInformation(tourDto.getInformation());
    }
}
