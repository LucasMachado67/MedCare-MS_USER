package com.example.medcare.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.medcare.models.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String>{


    @Query(value = """
            SELECT company_name FROM Company WHERE id = :id
            """, nativeQuery = true)
    String findCompanyNameById(@Param("id") String tenantId);
}
