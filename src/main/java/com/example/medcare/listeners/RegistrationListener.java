package com.example.medcare.listeners;

import com.example.medcare.enums.UserRole;
import com.example.medcare.events.UserRegisterEvent;
import com.example.medcare.services.UserAuthenticationService;
import com.example.medcare.models.User;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class RegistrationListener {

    @Autowired
    private UserAuthenticationService service;

    @RabbitListener(queues = "${medcare.rabbitmq.queue.medic-registered}")
    public void listenMedicCreationQueue(@Payload UserRegisterEvent userRegisterEvent){
        var user = new User();

        user.setPersonId(userRegisterEvent.person_id());
        user.setUsername(userRegisterEvent.username());
        user.setRole(UserRole.MEDIC);

        service.createUserCredentials(user.getPersonId(), user.getUsername(), String.valueOf(user.getRole()));
    }

    @RabbitListener(queues = "${medcare.rabbitmq.queue.patient-registered}")
    public void listenPatientCreationQueue(@Payload UserRegisterEvent userRegisterEvent){
        var user = new User();

        user.setPersonId(userRegisterEvent.person_id());
        user.setUsername(userRegisterEvent.username());
        user.setRole(UserRole.USER);

        service.createUserCredentials(user.getPersonId(), user.getUsername(), String.valueOf(user.getRole()));
    }
}
