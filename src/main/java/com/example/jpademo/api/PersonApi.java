package com.example.jpademo.api;


import com.example.jpademo.service.dtos.PersonDto;
import com.example.jpademo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "person")
public class PersonApi {

    @Autowired
    private PersonService personService;

    @GetMapping
    public List<PersonDto> getAllPersons() {
        return personService.getAllPersons();
    }

    @GetMapping("/name/{name}")
    public List<PersonDto> getPersonByName(@PathVariable String name) { return personService.getPersonByName(name); }

    @PostMapping
    public void insertNewPerson(@RequestBody PersonDto person) {
        personService.saveNewPerson(person);
    }

}
