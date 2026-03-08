package com.example.medcare.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.medcare.dto.CompanyRegisterDTO;
import com.example.medcare.enums.UserRole;
import com.example.medcare.events.CompanyCreatedEvent;
import com.example.medcare.models.Company;
import com.example.medcare.models.User;
import com.example.medcare.producer.CompanyProducer;
import com.example.medcare.producer.UserProducer;
import com.example.medcare.repositories.CompanyRepository;
import com.example.medcare.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.transaction.Transactional;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProducer userProducer;

    @Autowired
    private CompanyProducer companyProducer;

    @Transactional
    public void registerCompany(CompanyRegisterDTO dto) throws JsonProcessingException{

        Company comp = new Company();
        comp.setId(UUID.randomUUID().toString());
        comp.setCompanyName((dto.getCompanyName()));
        comp.setActive(dto.isActive());

        repository.save(comp);

        User admin = new User();
        admin.setEmail(dto.getAdminEmail());
        String encryptedPassword = new BCryptPasswordEncoder().encode(dto.getPassword());
        admin.setPassword(encryptedPassword);
        admin.setTenantId(comp.getId());
        admin.setRole(UserRole.ADMIN);

        userRepository.save(admin);

        //Evento de envio de e-mail para o serviço de notificação sobre o 'user' ADMIN
        userProducer.publishMessageEmail(admin, dto.getPassword());
        //Evento de criação dos dados restantes da empresa no sistema de entidades
        CompanyCreatedEvent event = new CompanyCreatedEvent(
            comp.getId(),
            comp.getCompanyName()
        );
        companyProducer.sendCompanyInfo(event);
    }

    public String getCompanyNameById(String tenantId){
       return repository.findCompanyNameById(tenantId);
    }
}
