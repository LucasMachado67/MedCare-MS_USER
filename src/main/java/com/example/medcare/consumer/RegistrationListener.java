package com.example.medcare.consumer;

import com.example.medcare.events.UserRegisterEvent;
import com.example.medcare.services.UserAuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.awspring.cloud.sqs.annotation.SqsListener;

import com.example.medcare.models.User;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ObjectMapper objectMapper;

    @SqsListener(value = "${medcare.aws.sqs.queue.entity.register}")
    public void listenUserRegistrationQueue(String payload) {
        try {
            // Convertendo o JSON para o objeto de evento
            UserRegisterEvent event = objectMapper.readValue(payload, UserRegisterEvent.class);

            System.out.println("Processando registry para: " + event.username() + " com role: " + event.role());

            service.createUserCredentials(
                    event.person_id(),
                    event.username(),
                    event.role(),
                    event.tenantID()
            );

        } catch (Exception e) {
            System.err.println("Erro ao processar evento de registro: " + e.getMessage());
        }
    }
}
