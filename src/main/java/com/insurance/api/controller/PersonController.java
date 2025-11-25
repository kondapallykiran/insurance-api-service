package com.insurance.api.controller;

import com.insurance.api.dto.PersonRequest;
import com.insurance.api.dto.PersonResponse;
import com.insurance.api.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
@CrossOrigin(origins = "http://localhost:3000")  // React dev server
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<PersonResponse> createPerson(@Valid @RequestBody PersonRequest request) {
        PersonResponse response = personService.createPerson(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> getPersonById(@PathVariable Long id) {
        PersonResponse response = personService.getPersonById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PersonResponse>> getAllPersons(
            @RequestParam(name = "search", required = false) String search) {
        List<PersonResponse> persons = personService.getAllPersons(search);
        return ResponseEntity.ok(persons);
    }
}
