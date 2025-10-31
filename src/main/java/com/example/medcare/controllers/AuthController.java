package com.example.medcare.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.medcare.dto.AuthenticationDTO;
import com.example.medcare.dto.LoginResponseDTO;
import com.example.medcare.dto.RegisterRequestDTO;
import com.example.medcare.dto.UserResponseDto;
import com.example.medcare.models.User;
import com.example.medcare.repositories.UserRepository;
import com.example.medcare.services.UserAuthenticationService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



/**
 * Controlador REST responsável por todas as operações relacionadas à autenticação
 * e gerenciamento de conta do usuário no microsserviço ms_user.
 * Inclui endpoints para login, registro e recuperação de dados do usuário autenticado.
 */
@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private UserAuthenticationService authenticationService;
    @Autowired
    private UserRepository repository;

    /**
     * Endpoint para autenticação de usuário (login).
     *
     * Recebe as credenciais (username e password), autentica o usuário
     * e retorna um JWT (JSON Web Token) para uso em requisições futuras.
     *
     * @param authenticationDTO DTO contendo o username e a senha para autenticação.
     * @return ResponseEntity contendo o LoginResponseDTO (token, username, personId)
     * e o status HTTP 200 OK.
     * @throws org.springframework.security.authentication.BadCredentialsException se as credenciais forem inválidas.
     */
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Valid AuthenticationDTO authenticationDTO) {
        authenticationService.loadUserByUsername(authenticationDTO.username());
        return authenticationService.login(authenticationDTO);
        // O Service retorna o DTO, e o Controller o envolve em 200 OK
        // return ResponseEntity.ok(authenticationService.login(authenticationDTO, authenticationManager));
    }

    /**
     * Endpoint para registrar um novo usuário no sistema.
     *
     * O método recebe os dados do usuário, criptografa a senha e persiste a nova conta.
     *
     * @param registerRequestDTO DTO contendo os dados para o novo usuário (username, password, role, personId).
     * @return ResponseEntity com status HTTP 201 Created e corpo vazio, indicando sucesso.
     * @throws com.hospital.user.exception.UsernameAlreadyExistsException (409 Conflict) se o username já estiver cadastrado.
     * @throws org.springframework.web.bind.MethodArgumentNotValidException (400 Bad Request) se a validação do DTO falhar.
     */
    @PostMapping("/signup")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequestDTO registerRequestDTO) {

        authenticationService.register(registerRequestDTO);
        // Retorna 201 Created, que é o padrão REST para criação bem-sucedida.
        return ResponseEntity.status(HttpStatus.CREATED).build();
    
    }

    /**
     * Endpoint para recuperar os dados do usuário atualmente autenticado.
     *
     * Este método é protegido por filtro de segurança (via JWT) e retorna
     * os detalhes da entidade User logada.
     *
     * @return ResponseEntity contendo a Entidade User (que pode ser mapeada para um DTO de resposta)
     * e o status HTTP 200 OK.
     */
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        User user = authenticationService.getAuthenticatedUser();
        return ResponseEntity.ok(user);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = repository.findAll();

        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/{personId}")
    public ResponseEntity<UserResponseDto> findByPersonId(@PathVariable long personId){
        UserResponseDto foundUser = authenticationService.findByPersonId(personId);
        if (foundUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundUser);      
    }
    
    
}
