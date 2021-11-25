package com.example.services;

import com.example.dto.PersonDto;
import com.example.dto.PersonEntity;

public interface PersonService {
    PersonEntity addPerson(PersonDto personDto);
    PersonEntity findByNationalCode(String nationalCode);
    PersonEntity updatePerson(PersonDto personDto, String nationalCode);
    boolean deletePersonByNationalCode(String nationalCode);
}
