package com.example.services.impl;

import com.example.dto.PersonDto;
import com.example.dto.PersonEntity;
import com.example.exceptions.ResourceDuplicationException;
import com.example.exceptions.ResourceNotFoundException;
import com.example.repositories.PersonRepository;
import com.example.services.PersonService;
import org.apache.camel.Body;
import org.apache.camel.Header;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

@ApplicationScoped
@Named(value = "personService")
public class PersonServiceImpl implements PersonService {
    @Inject
    PersonRepository personRepository;

    @Override
    public PersonEntity addPerson(@Body PersonDto personDto) {
        if (personRepository.isPersonExist(personDto.getNationalCode())){
            throw new ResourceDuplicationException(String.format("Person with nationalCode %s already exist",personDto.getNationalCode()));
        }
        return this.personRepository.addPerson(createEntity(personDto));
    }

    @Override
    public PersonEntity findByNationalCode(@Header(value = "nationalCode") String nationalCode) {
        if (!personRepository.isPersonExist(nationalCode)){
            throw new ResourceNotFoundException(String.format("Person with nationalCode %s does not exist",nationalCode));
        }
        return this.personRepository.findByNationalCode(nationalCode);
    }

    @Override
    public PersonEntity updatePerson(@Body PersonDto personDto,@Header(value = "nationalCode") String nationalCode) {
        if (!personRepository.isPersonExist(nationalCode)){
            throw new ResourceNotFoundException(String.format("Person with nationalCode %s does not exist",nationalCode));
        }
        return this.personRepository.updatePerson(personDto,nationalCode);
    }

    @Override
    public boolean deletePersonByNationalCode(@Header(value = "nationalCode") String nationalCode) {
        if (!personRepository.isPersonExist(nationalCode)){
            throw new ResourceNotFoundException(String.format("Person with nationalCode %s does not exist",nationalCode));
        }
        return this.personRepository.deletePersonByNationalCode(nationalCode);
    }

    private PersonEntity createEntity(PersonDto personDto){
        PersonEntity personEntity = new PersonEntity();
        personEntity.setFirstName(personDto.getFirstName());
        personEntity.setLastName(personDto.getLastName());
        personEntity.setAge(personDto.getAge());
        personEntity.setNationalCode(personDto.getNationalCode());
        personEntity.setMobile(personDto.getMobile());
        personEntity.setEmail(personDto.getEmail());
        return personEntity;
    }
}
