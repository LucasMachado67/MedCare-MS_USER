package com.example.medcare.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.medcare.models.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String>{


}
