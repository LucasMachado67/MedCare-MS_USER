package com.example.medcare.controllers;


import com.example.medcare.dto.CompanyRegisterDTO;
import com.example.medcare.services.CompanyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("company")
public class CompanyController {

    @Autowired
    private CompanyService service;

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> create(@Valid @RequestBody CompanyRegisterDTO dto) throws JsonProcessingException {
        service.registerCompany(dto);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }
}
