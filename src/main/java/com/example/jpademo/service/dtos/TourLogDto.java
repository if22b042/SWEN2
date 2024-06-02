package com.example.jpademo.service.dtos;

import javafx.beans.property.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TourLogDto {

    private LongProperty id = new SimpleLongProperty();
    private ObjectProperty<LocalDateTime> dateTime = new SimpleObjectProperty<>();
    private StringProperty comment = new SimpleStringProperty();
    private StringProperty difficulty = new SimpleStringProperty();
    private IntegerProperty totalDistance = new SimpleIntegerProperty();
    private IntegerProperty totalTime = new SimpleIntegerProperty();
    private IntegerProperty rating = new SimpleIntegerProperty();
    private LongProperty tourId = new SimpleLongProperty();
    private StringProperty imagePath = new SimpleStringProperty(); // New property for image path

    // Getters and setters for JavaFX properties
    public Long getId() {
        return id.get();
    }

    public void setId(Long id) {
        this.id.set(id);
    }

    public LongProperty idProperty() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime.get();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime.set(dateTime);
    }

    public ObjectProperty<LocalDateTime> dateTimeProperty() {
        return dateTime;
    }

    public String getComment() {
        return comment.get();
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }

    public StringProperty commentProperty() {
        return comment;
    }

    public String getDifficulty() {
        return difficulty.get();
    }

    public void setDifficulty(String difficulty) {
        this.difficulty.set(difficulty);
    }

    public StringProperty difficultyProperty() {
        return difficulty;
    }

    public int getTotalDistance() {
        return totalDistance.get();
    }

    public void setTotalDistance(int totalDistance) {
        this.totalDistance.set(totalDistance);
    }

    public IntegerProperty totalDistanceProperty() {
        return totalDistance;
    }

    public int getTotalTime() {
        return totalTime.get();
    }

    public void setTotalTime(int totalTime) {
        this.totalTime.set(totalTime);
    }

    public IntegerProperty totalTimeProperty() {
        return totalTime;
    }

    public int getRating() {
        return rating.get();
    }

    public void setRating(int rating) {
        this.rating.set(rating);
    }

    public IntegerProperty ratingProperty() {
        return rating;
    }

    public Long getTourId() {
        return tourId.get();
    }

    public void setTourId(Long tourId) {
        this.tourId.set(tourId);
    }

    public LongProperty tourIdProperty() {
        return tourId;
    }

    // New getter and setter for imagePath
    public String getImagePath() {
        return imagePath.get();
    }

    public void setImagePath(String imagePath) {
        this.imagePath.set(imagePath);
    }

    public StringProperty imagePathProperty() {
        return imagePath;
    }
}
