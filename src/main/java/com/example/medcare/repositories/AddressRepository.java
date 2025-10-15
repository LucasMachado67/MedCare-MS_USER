package com.example.medcare.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.medcare.models.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long>{
    
}
