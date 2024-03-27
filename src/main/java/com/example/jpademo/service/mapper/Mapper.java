package com.example.jpademo.service.mapper;

public interface Mapper<S, T> {

    T mapToDto(S source);

}
