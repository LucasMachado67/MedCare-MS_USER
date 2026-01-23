package com.example.medcare.consumer;

import com.example.medcare.enums.UserRole;
import com.example.medcare.events.UserRegisterEvent;
import com.example.medcare.services.UserAuthenticationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.awspring.cloud.sqs.annotation.SqsListener;

import com.example.medcare.models.User;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Listener responsável por consumir eventos de criação de usuários provenientes
 * de outros microsserviços (como cadastro de médicos e pacientes).
 *
 * <p>
 * Ao receber o evento {@link UserRegisterEvent} pela fila RabbitMQ
 * correspondente,
 * o listener cria uma entidade {@link User} básica e delega ao serviço
 * {@link UserAuthenticationService} a responsabilidade de gerar as credenciais
 * de autenticação.
 * </p>
 *
 * <p>
 * Este componente faz parte do fluxo de integração assíncrona entre os
 * microsserviços do sistema, garantindo que cada novo usuário cadastrado em
 * outro contexto (paciente, médico, etc.) também seja registrado no serviço
 * de autenticação.
 * </p>
 */
@Component
public class RegistrationListener {

    @Autowired
    private UserAuthenticationService service;

    /**
     * Ouve a fila de cadastro de médicos e cria automaticamente as credenciais
     * para o novo usuário com o papel {@link UserRole#MEDIC}.
     *
     * @param userRegisterEvent evento contendo personId, username e role originais
     */
    @SqsListener("MedicQueue")
    public void listenMedicCreationQueue(String payload) {
        try {
            UserRegisterEvent event =
                new ObjectMapper().readValue(payload, UserRegisterEvent.class);

            service.createUserCredentials(
                event.person_id(),
                event.username(),
                "MEDIC");
        } catch (Exception e) {
            System.err.println("Erro ao processar evento: " + e.getMessage());
        }
    }

    /**
     * Ouve a fila de cadastro de pacientes e cria automaticamente as credenciais
     * para o novo usuário com o papel {@link UserRole#USER}.
     *
     * @param userRegisterEvent evento contendo personId, username e role originais
     * @throws JsonProcessingException 
     */
    @RabbitListener(queues = "${medcare.rabbitmq.queue.patient-registered}")
    public void listenPatientCreationQueue(@Payload UserRegisterEvent userRegisterEvent) throws JsonProcessingException {
        var user = new User();

        user.setPersonId(userRegisterEvent.person_id());
        user.setEmail(userRegisterEvent.username());
        user.setRole(UserRole.USER);

        service.createUserCredentials(user.getPersonId(), user.getUsername(), String.valueOf(user.getRole()));
    }

    /**
     * Ouve a fila de cadastro de assistentes e cria automaticamente as credenciais
     * para o novo usuário com o papel {@link UserRole#USER}.
     *
     * @param userRegisterEvent evento contendo personId, username e role originais
     * @throws JsonProcessingException 
     */
    @RabbitListener(queues = "${medcare.rabbitmq.queue.assistant-registered}")
    public void listenAssistantCreationQueue(@Payload UserRegisterEvent userRegisterEvent) throws JsonProcessingException {
        var user = new User();

        user.setPersonId(userRegisterEvent.person_id());
        user.setEmail(userRegisterEvent.username());
        user.setRole(UserRole.ASSISTANT);

        service.createUserCredentials(user.getPersonId(), user.getUsername(), String.valueOf(user.getRole()));
    }
}
