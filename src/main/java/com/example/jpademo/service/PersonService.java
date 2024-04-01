package com.example.jpademo.service;

import com.example.jpademo.service.dtos.PersonDto;

import java.util.List;

public interface   PersonService {

    void saveNewPerson(PersonDto personDto);
    List<PersonDto> getAllPersons();
    List<PersonDto> getPersonByName(String name);

    PersonDto updatePerson(Long id, PersonDto personDto);
}
