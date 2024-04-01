package com.example.jpademo.service.dtos;

import lombok.Data;

@Data
public class TourDto {
    private Long id;
    private String title;
    private String description;
    private Double price;
}
