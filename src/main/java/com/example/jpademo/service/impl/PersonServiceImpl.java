package com.example.jpademo.service.impl;

import com.example.jpademo.persistence.entities.PersonEntity;
import com.example.jpademo.persistence.repositories.PersonRepository;
import com.example.jpademo.service.PersonService;
import com.example.jpademo.service.dtos.PersonDto;
import com.example.jpademo.service.mapper.PersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PersonMapper personMapper;

    @Override
    public void saveNewPerson(PersonDto personDto) {
        PersonEntity entity = PersonEntity.builder()
                .name(personDto.getName())
                .email(personDto.getEmail())
                .age(personDto.getAge())
                .build();
        personRepository.save(entity);
    }

    @Override
    public List<PersonDto> getAllPersons() {
        return personMapper.mapToDto((Collection<PersonEntity>) personRepository.findAll());
    }

    @Override
    public List<PersonDto> getPersonByName(String name) {
        return personMapper.mapToDto(personRepository.findByNameIgnoreCase(name));
    }
    @Override
    @Transactional
    public PersonDto updatePerson(Long id, PersonDto personDto) {
        PersonEntity personEntity = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found"));

        personEntity.setName(personDto.getName());
        personEntity.setEmail(personDto.getEmail());
        personEntity.setAge(personDto.getAge());


        personRepository.save(personEntity);

        return personMapper.mapToDto(personEntity);
    }


}
