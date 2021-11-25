package com.example.repositories;

import com.example.dto.PersonDto;
import com.example.dto.PersonEntity;

public interface PersonRepository {
    PersonEntity addPerson(PersonEntity personEntity);
    PersonEntity findByNationalCode(String nationalCode);
    PersonEntity updatePerson(PersonDto personDto, String nationalCode);
    boolean deletePersonByNationalCode(String nationalCode);
    boolean isPersonExist(String nationalCode);
}
