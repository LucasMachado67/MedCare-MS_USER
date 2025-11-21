package com.example.medcare.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.medcare.dto.EmailDto;
import com.example.medcare.models.User;

/**
 * Componente responsável por publicar mensagens relacionadas ao usuário
 * para a fila de envio de e-mails. Utiliza RabbitMQ para enviar um
 * {@link EmailDto} para a fila configurada no sistema.
 *
 * <p>Esta classe é utilizada quando um novo usuário é cadastrado no sistema.
 * Ela monta o e-mail padrão de boas-vindas contendo as credenciais
 * iniciais e envia para a fila que será consumida pelo serviço de e-mail.</p>
 */
@Component
public class UserProducer {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Nome da fila configurada no application.properties onde a mensagem será publicada.
     */
    @Value(value = "${broker.queue.email.name}")//Default exchange type
    private String routingKey;
    
    /**
     * Publica uma mensagem na fila de e-mails informando que o usuário
     * foi cadastrado com sucesso. O método cria o {@link EmailDto}
     * contendo o destinatário, assunto e corpo da mensagem e envia 
     * para a fila configurada usando o {@link RabbitTemplate}.
     *
     * @param user O usuário recém cadastrado. Necessário para extrair o ID e o username.
     * @param initialPassword A senha inicial gerada automaticamente no registro.
     *
     * <p>O e-mail enviado contém:</p>
     * <ul>
     *   <li>Mensagem de boas-vindas</li>
     *   <li>Username do usuário</li>
     *   <li>Senha inicial gerada pelo sistema</li>
     * </ul>
     *
     * <p>Exemplo de envio:</p>
     * <pre>
     * rabbitTemplate.convertAndSend("", routingKey, emailDto);
     * </pre>
     */
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

