package com.example.medcare.dto;

import com.example.medcare.enums.UserRole;

import jakarta.validation.constraints.NotNull;


/**
 * DTO utilizado para realizar o registro de um novo usuário no sistema.
 *
 * <p>
 * Este objeto contém as informações necessárias para criar uma nova conta
 * de acesso, vinculada a uma entidade "Person" previamente cadastrada no
 * sistema de entidades.
 * </p>
 *
 * <p><b>Campos:</b></p>
 * <ul>
 *     <li><b>username</b>: e-mail ou identificador único usado para login.</li>
 *     <li><b>password</b>: senha que será criptografada antes do armazenamento.</li>
 *     <li><b>role</b>: papel do usuário no sistema (por exemplo: ADMIN, MEDIC, PATIENT).</li>
 *     <li><b>personId</b>: ID da pessoa (Person) associada a este usuário.</li>
 * </ul>
 *
 * <p><b>Fluxo de uso:</b></p>
 * <ul>
 *     <li>O cliente envia estes dados para a API de registro.</li>
 *     <li>O serviço valida o username e verifica se já existe usuário cadastrado.</li>
 *     <li>A senha é criptografada e o usuário é salvo no banco.</li>
 * </ul>
 *
 * @param email Email do usuário, obrigatório.
 * @param password Senha em texto puro, que será criptografada, obrigatória.
 * @param role Papel (role) do usuário dentro do sistema.
 * @param personId Identificador da entidade Person vinculada ao usuário.
 */
public class RegisterRequestDTO {

    
    @NotNull
    private String email;
    
    @NotNull
    private String password;
    
    @NotNull
    private UserRole userRole;

    private long personId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }


    
}
