package com.example.jpademo.persistence.repositories;

import com.example.jpademo.persistence.entities.AddressEntity;
import com.example.jpademo.persistence.entities.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AddressRepository extends CrudRepository<AddressEntity, Long> {

    List<AddressEntity> findByPerson(PersonEntity person);

}