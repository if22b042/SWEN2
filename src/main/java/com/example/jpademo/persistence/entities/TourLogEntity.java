package com.example.jpademo.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "TOUR_LOG")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TourLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateTime;
    private String comment;
    private String difficulty;
    private int totalDistance;
    private int totalTime;
    private int rating;
    private byte[] image;
    private String imagePath;
    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false)
    private TourEntity tour;


    // Getters and setters

}
