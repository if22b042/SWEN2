package com.example.jpademo.service.dtos;

import lombok.Data;

@Data
public class TourDto {
    private Long id;

    private String title;
    private String description;
    private String startLocation;
    private String endLocation;
    private String transportation;
    private int distance;
    private int time;
    private String information;
}
