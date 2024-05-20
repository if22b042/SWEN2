package com.example.jpademo.service.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
