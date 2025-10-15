package com.example.medcare.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.medcare.dto.AuthenticationDTO;
import com.example.medcare.dto.LoginResponseDTO;
import com.example.medcare.dto.RegisterRequestDTO;
import com.example.medcare.enums.UserRole;
import com.example.medcare.exceptions.UsernameAlreadyExistsException;
import com.example.medcare.models.User;
import com.example.medcare.repositories.UserRepository;
import com.example.medcare.security.TokenService;

import jakarta.validation.Valid;

/**
 * Serviço responsável pela lógica de autenticação (login/registro) e gerenciamento de usuários.
 * Implementa UserDetailsService para integração com o Spring Security.
 */

@Service
public class UserAuthenticationService implements UserDetailsService{


    //Injeção do repositoory para acesso ao banco de dados
    @Autowired
    private UserRepository userRepository;

    //Injeção do service JWT para geração de tokens
    @Autowired
    private TokenService tokenService;

    @Autowired
    private ApplicationContext context;


     /**
     * Carrega o usuário a partir do username (necessário pelo Spring Security).
     * @param username o nome de usuário (único) a ser buscado.
     * @return O objeto UserDetails (sua Entidade User) contendo os detalhes do usuário.
     * @throws UsernameNotFoundException se o usuário não for encontrado.
     */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        // Busca o usuário. O Spring Security espera que este método lance uma exceção
        // se o usuário não existir.
        return userRepository.findByUsername(username);
    }

     /**
     * Realiza o processo de autenticação e gera o Token JWT para o usuário.
     * @param data DTO contendo o username e a senha.
     * @return LoginResponseDTO contendo o token JWT, username e personId.
     */
    public LoginResponseDTO login(@RequestBody @Valid AuthenticationDTO data){
        AuthenticationManager authenticationManager = context.getBean(AuthenticationManager.class);
        // Cria o token de autenticação sem estar autenticado
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        // Autentica o usuário. Se falhar (senha incorreta), lança uma exceção pelo Spring Security.
        var auth = authenticationManager.authenticate(usernamePassword);
        // Pega o objeto User autenticado
        var user = (User) auth.getPrincipal();
        // Gera o token JWT
        var token = tokenService.generateToken(user);

        List<UserRole> userRoles = user.getRoles();

        // Retorna o DTO de resposta
        return new LoginResponseDTO(
                token,
                user.getUsername(),
                user.getPersonId(),
                userRoles.stream().map(UserRole::toString).collect(Collectors.toList())
        );
    }

    /**
     * Registra um novo usuário no sistema.
     * @param registerDTO DTO contendo os dados do novo usuário.
     * @throws UsernameAlreadyExistsException se o nome de usuário já estiver em uso.
     * @return ResponseEntity.ok().build() para indicar sucesso (200 OK ou 201 Created no Controller).
     */
    public void register(RegisterRequestDTO registerDTO){

        // Validação: Verifica se o usuário já existe
        if (this.userRepository.findByUsername(registerDTO.username()) != null) {
            throw new UsernameAlreadyExistsException(registerDTO.username());
        }
        // Criptografa a senha antes de salvar no banco de dados
        String encryptedPassword = new BCryptPasswordEncoder().encode(registerDTO.password());

        // Cria a nova entidade User
        User newUser = new User();
        newUser.setUsername(registerDTO.username());
        newUser.setPassword(encryptedPassword);
        newUser.setPersonId(registerDTO.personId());
        newUser.setRoles(registerDTO.roles());
        // Salva o novo usuário
        userRepository.save(newUser);
    }

    /**
     * Recupera o objeto User do contexto de segurança do Spring (após o filtro JWT).
     * Útil para obter o ID e papéis do usuário logado em rotas protegidas.
     * @return O objeto User autenticado.
     * @throws RuntimeException se não houver usuário autenticado no contexto (embora o filtro geralmente evite isso).
     */
    public User getAuthenticatedUser(){
        // Pega a autenticação do contexto de segurança
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         // Verifica se o usuário está autenticado
        if(auth == null || !auth.isAuthenticated()){
            throw new RuntimeException("User not authenticated");
        }
        // Retorna o objeto principal (a Entidade User)
        return (User) auth.getPrincipal();
    }

}
