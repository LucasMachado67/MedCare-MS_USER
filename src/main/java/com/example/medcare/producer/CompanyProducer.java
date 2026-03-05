package com.example.medcare.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.medcare.dto.EmailDto;
import com.example.medcare.events.CompanyCreatedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.awspring.cloud.sqs.operations.SqsTemplate;

/**
 * Componente responsável por publicar mensagens relacionadas a criação de uma empresa
 * para a fila de envio de e-mails. Utiliza Amazon SQS para enviar um
 * {@link EmailDto} para a fila configurada no sistema.
 *
 * <p>Esta classe é utilizada quando uma nova empresa é cadastrado no sistema.
 * Ela monta o e-mail padrão de boas-vindas contendo as credenciais do user ADMIN
 * iniciais e envia para a fila que será consumida pelo serviço de e-mail.</p>
 */
@Component
public class CompanyProducer {
    

    @Autowired
    private SqsTemplate sqsTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Value(value = "${medcare.aws.sqs.queue.companty.register}")
    private String CompanyCreationQueue;

    public void sendCompanyInfo(CompanyCreatedEvent event) throws JsonProcessingException{

        System.out.println("Enviando evento de criação para o tenant: " + event.id());
        String json = objectMapper.writeValueAsString(event);
        sqsTemplate.send(to -> to
                .queue(CompanyCreationQueue)
                .payload(json));
                System.out.println("Mensagem enviada");

    }
}
