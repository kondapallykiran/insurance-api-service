package com.insurance.api.service.impl;

import com.insurance.api.dto.AddressDto;
import com.insurance.api.dto.PersonRequest;
import com.insurance.api.dto.PersonResponse;
import com.insurance.api.entity.Address;
import com.insurance.api.entity.Person;
import com.insurance.api.exception.ResourceNotFoundException;
import com.insurance.api.repository.PersonRepository;
import com.insurance.api.service.PersonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public PersonResponse createPerson(PersonRequest request) {
        Person person = mapToEntity(request);
        Person saved = personRepository.save(person);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PersonResponse getPersonById(Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with id: " + id));
        return mapToResponse(person);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonResponse> getAllPersons(String search) {
        List<Person> persons;
        if (search != null && !search.isBlank()) {
            persons = personRepository
                    .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                            search, search, search);
        } else {
            persons = personRepository.findAll();
        }

        return persons.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private Person mapToEntity(PersonRequest request) {
        Person person = new Person();
        person.setFirstName(request.getFirstName());
        person.setLastName(request.getLastName());
        person.setEmail(request.getEmail());
        person.setPhoneNumber(request.getPhoneNumber());

        if (request.getDateOfBirth() != null && !request.getDateOfBirth().isBlank()) {
            person.setDateOfBirth(LocalDate.parse(request.getDateOfBirth()));
        }

        if (request.getAddress() != null) {
            AddressDto dto = request.getAddress();
            Address address = new Address();
            address.setLine1(dto.getLine1());
            address.setLine2(dto.getLine2());
            address.setCity(dto.getCity());
            address.setState(dto.getState());
            address.setPostalCode(dto.getPostalCode());
            address.setCountry(dto.getCountry());
            address.setAddressType(dto.getAddressType());
            person.setAddress(address);
        }

        return person;
    }

    private PersonResponse mapToResponse(Person person) {
        PersonResponse response = new PersonResponse();
        response.setPersonId(person.getPersonId());
        response.setFirstName(person.getFirstName());
        response.setLastName(person.getLastName());

        if (person.getDateOfBirth() != null) {
            response.setDateOfBirth(person.getDateOfBirth().toString());
        }

        response.setEmail(person.getEmail());
        response.setPhoneNumber(person.getPhoneNumber());

        if (person.getAddress() != null) {
            Address address = person.getAddress();
            AddressDto dto = new AddressDto();
            dto.setAddressId(address.getAddressId());
            dto.setLine1(address.getLine1());
            dto.setLine2(address.getLine2());
            dto.setCity(address.getCity());
            dto.setState(address.getState());
            dto.setPostalCode(address.getPostalCode());
            dto.setCountry(address.getCountry());
            dto.setAddressType(address.getAddressType());
            response.setAddress(dto);
        }

        return response;
    }
}
