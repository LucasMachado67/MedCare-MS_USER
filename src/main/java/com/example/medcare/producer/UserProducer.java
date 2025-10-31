package com.example.medcare.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.medcare.dto.EmailDto;
import com.example.medcare.models.User;

@Component
public class UserProducer {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value(value = "${broker.queue.email.name}")//Default exchange type
    private String routingKey;

    public void publishMessageEmail(User user, String initialPassword){
        var emailDto = new EmailDto();
        emailDto.setUserId(user.getId());
        emailDto.setEmailTo(user.getUsername());
        emailDto.setSubject("Cadastro realizado com sucesso");
        emailDto.setText("Cadastro de usuário realizado com sucesso seja bem vindo(a)! \nAgradecemos o seu cadastro." +
        "\n Seu usuário de login é: " + user.getUsername() +
        "\n Senha: " + initialPassword);

        rabbitTemplate.convertAndSend("", routingKey, emailDto);
    }
}   

