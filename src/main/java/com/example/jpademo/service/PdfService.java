package com.example.jpademo.service;

import com.example.jpademo.service.dtos.TourDto;
import com.example.jpademo.service.dtos.TourLogDto;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfService {

    @Autowired
    private TourService tourService;

    @Autowired
    private TourLogService tourLogService;

    public ByteArrayInputStream generatePdf() {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            List<TourDto> tours = tourService.getAllTours();
            for (TourDto tour : tours) {
                document.add(new Paragraph("Tour ID: " + tour.getId()));
                document.add(new Paragraph("Title: " + tour.getTitle()));
                document.add(new Paragraph("Description: " + tour.getDescription()));
                document.add(new Paragraph("Start Location: " + tour.getStartLocation()));
                document.add(new Paragraph("End Location: " + tour.getEndLocation()));
                document.add(new Paragraph("Transportation: " + tour.getTransportation()));
                document.add(new Paragraph("Distance: " + tour.getDistance()));
                document.add(new Paragraph("Time: " + tour.getTime()));
                document.add(new Paragraph("Information: " + tour.getInformation()));
                document.add(new Paragraph("Logs:"));

                List<TourLogDto> logs = tourLogService.getTourLogsByTourId(tour.getId());
                for (TourLogDto log : logs) {
                    document.add(new Paragraph("  Log ID: " + log.getId()));
                    document.add(new Paragraph("  Date: " + log.getDateTime()));
                    document.add(new Paragraph("  Comment: " + log.getComment()));
                    document.add(new Paragraph("  Difficulty: " + log.getDifficulty()));
                    document.add(new Paragraph("  Total Distance: " + log.getTotalDistance()));
                    document.add(new Paragraph("  Total Time: " + log.getTotalTime()));
                    document.add(new Paragraph("  Rating: " + log.getRating()));
                    document.add(new Paragraph(" "));
                }

                document.add(new Paragraph(" "));
            }

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
