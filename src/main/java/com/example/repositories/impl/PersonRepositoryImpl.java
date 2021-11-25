package com.example.repositories.impl;

import com.example.dto.PersonDto;
import com.example.dto.PersonEntity;
import com.example.repositories.PersonRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@Slf4j
@Named(value = "memoryRepository")
public class PersonRepositoryImpl implements PersonRepository {

    private final Map<String, PersonEntity> personDataSource = new ConcurrentHashMap<>();

    private volatile static int id = 0;

    @Inject
    ObjectMapper mapper;

    @Override
    public PersonEntity addPerson(PersonEntity personEntity) {
        nextId();
        personEntity.setId(id);
        try {
            log.info("Request for add Person ===> {}", mapper.writeValueAsString(personEntity));
        } catch (JsonProcessingException e) {
            log.info("Request for add Person ===> {}", personEntity);
        }
        this.personDataSource.put(personEntity.getNationalCode(), personEntity);
        return personEntity;
    }

    @Override
    public PersonEntity findByNationalCode(String nationalCode) {
        log.info("Request for find Person nationalCode ===> {}", nationalCode);
        return this.personDataSource.getOrDefault(nationalCode, null);
    }

    @Override
    public PersonEntity updatePerson(PersonDto personDto, String nationalCode) {
        try {
            log.info("Request for update Person  nationalCode {} ===> {}", nationalCode, mapper.writeValueAsString(personDto));
        } catch (JsonProcessingException e) {
            log.info("Request update Person  nationalCode {}  ===> {}", nationalCode, personDto);
        }

        PersonEntity personEntity = this.personDataSource.getOrDefault(nationalCode, null);
        try {
            log.info("Person with nationalCode {} ===> {}", nationalCode, mapper.writeValueAsString(personEntity));
        } catch (JsonProcessingException e) {
            log.info("Person with  nationalCode {}  ===> {}", nationalCode, personEntity);
        }
        if (personEntity != null) {
            personEntity.setFirstName(personDto.getFirstName());
            personEntity.setLastName(personDto.getLastName());
            personEntity.setAge(personDto.getAge());
            personEntity.setEmail(personDto.getEmail());
            personEntity.setMobile(personDto.getMobile());
            personDataSource.remove(nationalCode);
            personDataSource.put(nationalCode, personEntity);
            log.info("person with nationalCode {} updated  ",nationalCode);
            return personEntity;
        }
        return null;
    }


    @Override
    public boolean deletePersonByNationalCode(String nationalCode) {
        log.info("Request for delete Person nationalCode ===> {}", nationalCode);
        if (personDataSource.containsKey(nationalCode)) {
            personDataSource.remove(nationalCode);
            return true;
        }
        return false;
    }

    public static synchronized void nextId() {
        id++;
    }

    public boolean isPersonExist(String nationalCode) {
        return personDataSource.containsKey(nationalCode);
    }
}
