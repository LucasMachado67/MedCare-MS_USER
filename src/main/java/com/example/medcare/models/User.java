package com.example.medcare.models;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.medcare.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * Representa as credenciais de autenticação e autorização de um usuário do sistema.
 *
 * <p>Esta entidade é responsável por armazenar informações de login de forma
 * separada dos dados pessoais. Ela implementa {@link org.springframework.security.core.userdetails.UserDetails},
 * permitindo integração total com o Spring Security.</p>
 *
 * <p>Cada usuário possui um vínculo com uma entidade Person no serviço
 * de entidades, identificado pelo atributo {@code personId}. Isso garante
 * separação entre dados pessoais (nome, e-mail, etc.) e dados de autenticação
 * (usuário, senha, permissões).</p>
 */
@Entity
@Table(name = "user_credentials")
public class User implements UserDetails {
    
    /**
     * Identificador único da credencial de usuário.
     *
     * <p>Gerado utilizando UUID para garantir unicidade em ambientes distribuídos
     * ou arquiteturas de microsserviços.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    /**
     * Nome de usuário utilizado para login.
     *
     * <p>Este campo é obrigatório e deve ser único, embora a regra de
     * unicidade possa ser aplicada no banco de dados ou no serviço.</p>
     */
    @NotNull
    @Column(unique = true)
    private String username;
    /**
     * Senha criptografada do usuário.
     *
     * <p>A senha deve ser armazenada sempre de forma segura, utilizando o
     * {@code PasswordEncoder} configurado. Nunca deve ser salva em texto puro.</p>
     */
    @NotNull
    private String password;
     /**
     * Papel (role) que define as permissões de acesso do usuário.
     *
     * <p>O valor é armazenado como texto ({@link EnumType#STRING}), o que facilita
     * a leitura e manutenção do banco de dados. Exemplos: ADMIN, MEDIC, PATIENT.</p>
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    private UserRole role;
    /**
     * Identificador da entidade Person associada a este usuário.
     *
     * <p>Esse ID referencia o serviço de entidades, permitindo que o sistema de usuários
     * não armazene informações sensíveis como nome e e-mail.</p>
     *
     * <p>É único para garantir que cada pessoa tenha apenas uma conta de autenticação.</p>
     */
    @NotNull
    @Column(unique = true)
    private long personId;
    //Construtor padrão para o JPA
    public User(){}
    
    //Getters e Setters e métodos da interface UserDetails
    public UUID getId(){
        return this.id;
    }

    public UUID setId(){
        return this.id;
    }   

    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
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
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role), new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return this.username;
    }
}
