package com.example.jpademo.service.mapper;

import com.example.jpademo.persistence.entities.TourEntity;
import com.example.jpademo.service.dtos.TourDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface TourMapper {
    TourMapper INSTANCE = Mappers.getMapper(TourMapper.class);

    TourDto toDto(TourEntity entity);
    TourEntity toEntity(TourDto dto);
    void updateEntityFromDto(TourDto dto, @MappingTarget TourEntity entity);
}
