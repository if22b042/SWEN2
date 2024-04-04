package com.example.jpademo;

import  com.example.jpademo.persistence.entities.PersonEntity;
import com.example.jpademo.persistence.repositories.PersonRepository;
import jakarta.annotation.PostConstruct;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class JpaDemoApplication {

    @Autowired
    private PersonRepository personRepository;

    public static void main(String[] args) {
        SpringApplication.run(JpaDemoApplication.class, args);
    }




}
