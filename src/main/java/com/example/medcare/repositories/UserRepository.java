package com.example.medcare.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.medcare.models.User;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    User findByUsername(String username);

    @Transactional
    @Modifying
    @Query(value = """
               UPDATE users
               SET password = :password
               WHERE username = :username
            """, nativeQuery = true)
    void changePassword (@Param("password") String password,@Param("username") String username);

    User findByPersonId(long id);
}
