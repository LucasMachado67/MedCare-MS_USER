package com.example.medcare.events;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserRegisterEvent(

        @JsonProperty("person_id")
        Long person_id,
        String username,
        String role
) {}
