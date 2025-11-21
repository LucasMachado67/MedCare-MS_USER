package com.example.medcare.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * Classe de configuração do RabbitMQ para o serviço de autenticação.
 *
 * <p>Define as filas utilizadas para comunicação assíncrona entre os microserviços
 * do MedCare, permitindo o envio e recebimento de mensagens referentes ao
 * cadastro de médicos e pacientes.</p>
 *
 * <p>Também configura um conversor de mensagens baseado em JSON utilizando
 * o {@link Jackson2JsonMessageConverter}, garantindo que os objetos enviados e
 * recebidos pelas filas sejam serializados e desserializados corretamente.</p>
 */
@Configuration
public class RabbitMQConfig {

    @Value("${medcare.rabbitmq.queue.medic-registered}")
    private String medicQueue;

    @Value("${medcare.rabbitmq.queue.patient-registered}")
    private String patientQueue;

    /**
     * Cria a fila responsável por receber eventos de cadastro de médicos.
     *
     * <p>A fila é persistente (durable = true), garantindo que mensagens não
     * sejam perdidas caso o broker seja reiniciado.</p>
     *
     * @return instância da fila de médicos
     */
    @Bean
    public Queue medicRegisteredQueue() {
        return new Queue(medicQueue, true);
    }
    /**
     * Cria a fila responsável por receber eventos de cadastro de pacientes.
     *
     * <p>A fila também é persistente para manter a confiabilidade da comunicação.</p>
     *
     * @return instância da fila de pacientes
     */
    @Bean
    public Queue patientRegisteredQueue() {
        return new Queue(patientQueue, true);
    }
    /**
     * Configura o conversor de mensagens usado pelo RabbitMQ para transformar
     * mensagens JSON em objetos Java e vice-versa.
     *
     * <p>Utiliza um {@link ObjectMapper} do Jackson, permitindo compatibilidade
     * total com records, DTOs e entidades do projeto.</p>
     *
     * @return conversor de mensagens baseado em JSON
     */
   @Bean
   public Jackson2JsonMessageConverter messageConverter(){
       ObjectMapper objectMapper = new ObjectMapper();
       return new Jackson2JsonMessageConverter(objectMapper);
   }
}
