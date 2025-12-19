package com.example.medcare.dto;

import java.util.UUID;

import com.example.medcare.enums.UserRole;
/**
 * DTO de resposta utilizado para retornar informações públicas do usuário.
 *
 * <p>
 * Este objeto é geralmente utilizado em endpoints que retornam dados de usuários,
 * como consultas por ID, consultas por personId ou retorno após operações
 * administrativas.
 * </p>
 *
 * <p><b>Objetivo:</b></p>
 * Fornecer ao cliente apenas as informações necessárias e seguras sobre um usuário,
 * evitando expor dados sensíveis como senha.
 *
 * <p><b>Campos:</b></p>
 * <ul>
 *     <li><b>id</b>: identificador único do usuário no sistema.</li>
 *     <li><b>username</b>: nome de usuário utilizado para login (geralmente o e-mail).</li>
 *     <li><b>role</b>: papel do usuário no sistema (ADMIN, MEDIC, PATIENT, etc.).</li>
 *     <li><b>personId</b>: ID da entidade Person vinculada ao usuário.</li>
 * </ul>
 *
 * <p><b>Uso comum:</b></p>
 * <ul>
 *     <li>Retorno de consultas de usuários.</li>
 *     <li>Exibição de informações em dashboards administrativos.</li>
 *     <li>Confirmação de operações de criação/edição.</li>
 * </ul>
 *
 * @param id UUID identificador único do usuário.
 * @param username Nome de usuário utilizado no login.
 * @param role Papel do usuário no sistema.
 * @param personId ID da entidade Person associada a este usuário.
 */
public class UserResponseDto {

    private UUID id;
    private String email;
    private UserRole role;
    private long personId;
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public UserRole getRole() {
        return role;
    }
    public void setRole(UserRole role) {
        this.role = role;
    }
    public long getPersonId() {
        return personId;
    }
    public void setPersonId(long personId) {
        this.personId = personId;
    }

    
}
