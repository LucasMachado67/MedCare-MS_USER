package com.example.medcare.enums;

public enum UserRole {
    ADMIN("admin"),
    USER("user"),
    MEDIC("medic");
    private final String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
