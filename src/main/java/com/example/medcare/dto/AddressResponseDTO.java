package com.example.medcare.dto;


public class AddressResponseDTO {

        private Long id;
        private String neighborhood;
        private String street;
        private int number;
        private String complement;
        private String city;
        private String zipCode;
        private String state;
        private String habitation;

        
        public Long getId() {
                return id;
        }
        public void setId(Long id) {
                this.id = id;
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
        public String getComplement() {
                return complement;
        }
        public void setComplement(String complement) {
                this.complement = complement;
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
        public String getState() {
                return state;
        }
        public void setState(String state) {
                this.state = state;
        }
        public String getHabitation() {
                return habitation;
        }
        public void setHabitation(String habitation) {
                this.habitation = habitation;
        }

        

        
}
