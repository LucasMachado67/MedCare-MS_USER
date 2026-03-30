package com.example.medcare.events;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Evento publicado quando um novo usuário é registrado no sistema.
 * Este record representa os dados básicos necessários para que outros
 * serviços ou microsserviços possam reagir ao cadastro de um usuário.
 *
 * <p>Ele é utilizado, por exemplo, para integração assíncrona via mensageria
 * (RabbitMQ, Kafka, etc.), permitindo que sistemas externos executem ações
 * como envio de e-mail, criação de perfil, registro de permissões, etc.</p>
 *
 * <p>A anotação {@link JsonProperty} é utilizada para garantir a compatibilidade
 * do nome do campo person_id no formato JSON, independente do padrão de nomenclatura
 * utilizado em Java.</p>
 *
 * @param person_id ID da entidade Person associada ao usuário registrado.
 * @param username Nome de usuário utilizado no login.
 * @param role Papel/permissão do usuário (ex.: "ADMIN", "USER").
 */
public record UserRegisterEvent(

        @JsonProperty("person_id")
        Long person_id,
        String username,
        String role,
        String tenantId
) {}
