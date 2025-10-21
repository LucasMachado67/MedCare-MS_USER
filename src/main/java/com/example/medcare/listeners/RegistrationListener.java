package com.example.medcare.listeners;

import com.example.medcare.events.MedicRegisteredEvent;
import com.example.medcare.services.UserAuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

@Configuration
public class RegistrationListener {

    @Autowired
    private UserAuthenticationService service;

//    @Bean
//    public Consumer<MedicRegisteredEvent> handleRegistration(){
//        return event -> {
//
//            System.out.println("Auth Service: Evento de Registro Recebido para Person ID: " + event.person_id());
//
//            try {
//                // Chama o serviço para criar as credenciais do usuário
//                service.createUserCredentials(
//                        event.person_id(),
//                        event.email(),
//                        event.role()
//                        // NOTA: Você precisará de uma LÓGICA DE SENHA PADRÃO ou SENHA TEMPORÁRIA aqui,
//                        // já que o Auth Service precisa criar a senha criptografada.
//                );
//
//                System.out.println("Auth Service: Credenciais criadas com sucesso para " + event.email());
//            } catch (Exception e) {
//                // IMPORTANTE: Se o processamento falhar aqui, o RabbitMQ tentará reprocessar
//                // a mensagem (até o limite de tentativas). Use um DLQ para mensagens que falham permanentemente.
//                System.err.println("ERRO ao processar evento e criar credenciais: " + e.getMessage());
//                // Relance uma RuntimeException se a falha for transitória e você quiser reprocessamento.
//                // throw new RuntimeException("Falha no processamento, tentar novamente.");
//            }
//        };
//    }
@Bean
public Consumer<byte[]> handleRegistration(){
    return payload -> {
        String rawJson = new String(payload, StandardCharsets.UTF_8);
        System.out.println("------------------------------------------");
        System.out.println("RAW PAYLOAD RECEBIDO:");
        System.out.println(rawJson); // <<< ESTA LINHA VAI MOSTRAR O JSON QUE CHEGOU
        System.out.println("------------------------------------------");

        try {
            // Tente a conversão manual AQUI
            ObjectMapper mapper = new ObjectMapper();
            MedicRegisteredEvent event = mapper.readValue(rawJson, MedicRegisteredEvent.class);

            System.out.println("Auth Service: Evento de Registro Recebido para Person ID: " + event.person_id());
            // ... Lógica para criar usuário

        } catch (Exception e) {
            System.err.println("ERRO FATAL NA CONVERSÃO MANUAL DO EVENTO:");
            e.printStackTrace(); // <<< ISSO VAI MOSTRAR A CAUSA REAL DA FALHA
        }
    };
}
}
