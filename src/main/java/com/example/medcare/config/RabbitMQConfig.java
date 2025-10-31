package com.example.medcare.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RabbitMQConfig {

    @Value("${medcare.rabbitmq.queue.medic-registered}")
    private String medicQueue;

    @Value("${medcare.rabbitmq.queue.patient-registered}")
    private String patientQueue;


    @Bean
    public Queue medicRegisteredQueue() {
        return new Queue(medicQueue, true);
    }

    @Bean
    public Queue patientRegisteredQueue() {
        return new Queue(patientQueue, true);
    }

   @Bean
   public Jackson2JsonMessageConverter messageConverter(){
       ObjectMapper objectMapper = new ObjectMapper();
       return new Jackson2JsonMessageConverter(objectMapper);
   }
}
