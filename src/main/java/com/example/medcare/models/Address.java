package com.example.medcare.models;

import com.example.medcare.enums.Habitation;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String neighborhood;
    @NotNull
    private String street;
    @NotNull
    private int number;
    @NotNull
    private String complement;
    @NotNull
    private String city;
    @NotNull
    private String zipCode;
    @NotNull
    private String state;
    @NotNull
    private Habitation habitation;

    public Address(){}
    
    public Address(String neighborhood, String street, int number, String city, String zipCode, Habitation habitation, String state, String complement){
        setNeighborhood(neighborhood);
        setStreet(street);
        setNumber(number);
        setCity(city);
        setZipCode(zipCode);
        setComplement(complement);
        setState(state);
        setHabitation(habitation);
    }

    
    public String getNeighborhood() {
        return neighborhood;
    }
    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }
    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getZipCode() {
        return zipCode;
    }
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }  
    public Habitation getHabitation() {
        return habitation;
    }
    public void setHabitation(Habitation habitation) {
        this.habitation = habitation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    

}
