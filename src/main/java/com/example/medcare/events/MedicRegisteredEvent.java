package com.example.medcare.events;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MedicRegisteredEvent(

        @JsonProperty("person_id")
        Long person_id,
        String email,
        String role //"MEDIC"
) {

}
