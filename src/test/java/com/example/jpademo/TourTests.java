package com.example.jpademo;

import com.example.jpademo.persistence.entities.TourEntity;
import com.example.jpademo.persistence.repositories.TourRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class TourTests {

    @Autowired
    private TourRepository tourRepository;

    private TourEntity savedTour;

    @BeforeEach
    void setup() {
        TourEntity tour = new TourEntity();
        tour.setTitle("Vienna City Tour");
        tour.setDescription("A lovely tour around Vienna.");
        tour.setStartLocation("Karlsplatz");
        tour.setEndLocation("Schönbrunn");
        tour.setTransportation("Bus");
        tour.setDistance(15);
        tour.setTime(30);
        tour.setInformation("Guided tour with a visit to historical sites.");
        savedTour = tourRepository.save(tour);
    }

    @Test
    void createTourTest() {
        assertNotNull(savedTour);
        assertNotNull(savedTour.getId());
    }

    @Test
    void updateTourTest() {
        savedTour.setDescription("Updated description of the tour.");
        tourRepository.save(savedTour);

        TourEntity updatedTour = tourRepository.findById(savedTour.getId()).orElse(null);
        assertNotNull(updatedTour);
        assertEquals("Updated description of the tour.", updatedTour.getDescription());
    }

    @Test
    void findTourByIdTest() {
        TourEntity foundTour = tourRepository.findById(savedTour.getId()).orElse(null);
        assertNotNull(foundTour);
        assertEquals(savedTour.getId(), foundTour.getId());
    }
    @Test
    void deleteTourTest() {
        long countBeforeDeletion = tourRepository.count();

        tourRepository.delete(savedTour);

        Optional<TourEntity> deletedTour = tourRepository.findById(savedTour.getId());
        long countAfterDeletion = tourRepository.count();

        assertFalse(deletedTour.isPresent());
        assertEquals(countBeforeDeletion - 1, countAfterDeletion, "The tour count should decrease by one after deletion.");
    }
}
