package com.example.jpademo.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TOUR")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TourEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String from;
    private String to;
    private String transportation;
    private int distance;
    private int time;
    private String information;

}
