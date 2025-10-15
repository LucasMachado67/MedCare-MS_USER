package com.example.medcare.models;


import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="person")
public class Person {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private Date birthDate;
    @NotNull
    private String cpf;
    @NotNull
    private String gender;
    @NotNull
    private String email;
    @NotNull
    private String phone;
    @NotNull
    @OneToOne(cascade = CascadeType.ALL) // Ou @ManyToOne, dependendo da sua regra de negócio
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;
    

    public Person(String name, Date birthDate, String cpf, String gender, String email, String phone, Address address){
        setName(name);
        setBirthDate(birthDate);
        setCpf(cpf);
        setGender(gender);
        setEmail(email);
        setPhone(phone);
        setAddress(address);
    }

    public long getId(){
        return id;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Date getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
    
}
