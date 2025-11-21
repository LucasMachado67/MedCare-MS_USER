package com.example.medcare.dto;

import java.util.UUID;

/**
 * DTO responsável por transportar os dados necessários para o envio de e-mails
 * dentro da aplicação.
 * <p>
 * Este objeto é utilizado principalmente para comunicação entre serviços via
 * mensageria (RabbitMQ), contendo as informações essenciais para compor
 * uma mensagem de e-mail.
 * </p>
 */
public class EmailDto {

    /**
     * Identificador único do usuário associado ao envio do e-mail.
     */
    private UUID userId;
    /**
     * Endereço de e-mail do destinatário.
     */
    private String emailTo;
    /**
     * Assunto da mensagem de e-mail.
     */
    private String subject;
    /**
     * Conteúdo textual do e-mail a ser enviado.
     */
    private String text;

    // Getters e setters
    public UUID getUserId() {
        return userId;
    }
    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    public String getEmailTo() {
        return emailTo;
    }
    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    
}
