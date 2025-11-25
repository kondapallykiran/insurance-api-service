package com.insurance.api.service;

import com.insurance.api.dto.PersonRequest;
import com.insurance.api.dto.PersonResponse;

import java.util.List;

public interface PersonService {

    PersonResponse createPerson(PersonRequest request);

    PersonResponse getPersonById(Long id);

    List<PersonResponse> getAllPersons(String search);
}
