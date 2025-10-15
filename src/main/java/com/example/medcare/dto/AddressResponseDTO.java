package com.example.medcare.dto;


public class AddressResponseDTO {

        private Long id;
        private String district;
        private String road;
        private int number;
        private String city;
        private String cep;
        private String habitation;

        public Long getId() {
                return id;
        }
        public void setId(Long id) {
                this.id = id;
        }
        public String getDistrict() {
                return district;
        }
        public void setDistrict(String district) {
                this.district = district;
        }
        public String getRoad() {
                return road;
        }
        public void setRoad(String road) {
                this.road = road;
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
        public String getCep() {
                return cep;
        }
        public void setCep(String cep) {
                this.cep = cep;
        }
        public String getHabitation() {
                return habitation;
        }
        public void setHabitation(String habitation) {
                this.habitation = habitation;
        }

        
}
