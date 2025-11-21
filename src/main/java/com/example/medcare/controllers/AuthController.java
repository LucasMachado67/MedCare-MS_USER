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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.HttpHeaders;

/**
 * Controlador REST responsável pelas operações de autenticação e gerenciamento
 * de contas de usuário no microsserviço <b>ms_user</b>.
 *
 * <p>Fornece endpoints para:</p>
 * <ul>
 *     <li>Login</li>
 *     <li>Registro de novos usuários</li>
 *     <li>Validação de token</li>
 *     <li>Recuperação do usuário autenticado</li>
 *     <li>Busca de usuários por personId</li>
 *     <li>Listagem de todos os usuários (somente para administradores)</li>
 * </ul>
 *
 * <p>Os métodos utilizam DTOs específicos para garantir segurança e isolamento
 * entre API e entidades de domínio.</p>
 */
@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private UserAuthenticationService authenticationService;
    @Autowired
    private UserRepository repository;

    /**
     * Endpoint utilizado por outros microsserviços ou gateways para validar
     * rapidamente um token JWT recebido no header Authorization.
     *
     * <p>Este método não valida assinatura ou permissões — apenas verifica
     * a estrutura básica do header e se o token foi enviado.</p>
     *
     * @param authHeader valor enviado no header "Authorization" no formato "Bearer &lt;token&gt;"
     * @return 200 OK se o token estiver presente, 401 se inválido e 403 se ausente.
     */
    @GetMapping("/auth/validate")
    public ResponseEntity<Void> validateToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);

        if (token != null) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    /**
     * Endpoint responsável pelo processo de autenticação (login).
     *
     * <p>Recebe as credenciais do usuário, valida as informações e retorna um
     * token JWT que permitirá acesso aos endpoints protegidos do sistema.</p>
     *
     * @param authenticationDTO DTO contendo username e password.
     * @return {@link LoginResponseDTO} contendo o token JWT, username, id da pessoa e role.
     * @throws org.springframework.security.authentication.BadCredentialsException
     *         Caso o username ou senha estejam incorretos.
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
     * <p>O serviço verifica se o username já existe, criptografa a senha e cria
     * a conta no banco de dados. Este endpoint é utilizado principalmente por
     * outros microsserviços que registram usuários automaticamente (ex.: médicos
     * e pacientes).</p>
     *
     * @param registerRequestDTO DTO contendo username, senha, role e personId.
     * @return 201 Created caso o registro seja bem-sucedido.
     * @throws com.example.medcare.exceptions.UsernameAlreadyExistsException
     *         Caso o username informado já esteja em uso.
     */
    @PostMapping("/signup")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequestDTO registerRequestDTO) {

        authenticationService.register(registerRequestDTO);
        // Retorna 201 Created, que é o padrão REST para criação bem-sucedida.
        return ResponseEntity.status(HttpStatus.CREATED).build();
    
    }

    /**
     * Retorna os dados do usuário atualmente autenticado com base no token JWT.
     *
     * <p>Este endpoint é protegido por filtro JWT e retorna o objeto User
     * diretamente — podendo futuramente ser substituído por um DTO específico
     * para maior segurança.</p>
     *
     * @return Entidade {@link User} correspondente ao usuário autenticado.
     */
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        User user = authenticationService.getAuthenticatedUser();
        return ResponseEntity.ok(user);
    }
    /**
     * Retorna todos os usuários cadastrados no sistema.
     *
     * <p>Este endpoint deve ser utilizado somente por administradores, e pode
     * futuramente ser protegido por regras de acesso específicas.</p>
     *
     * @return Lista de usuários.
     */
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = repository.findAll();

        return ResponseEntity.ok(users);
    }
     /**
     * Busca um usuário pelo seu personId — utilizado principalmente pelos
     * microsserviços de médicos e pacientes para sincronização interna.
     *
     * @param personId ID da pessoa associada ao usuário.
     * @return DTO com ID, username, role e personId, ou 404 caso não exista.
     */
    @GetMapping("/{personId}")
    public ResponseEntity<UserResponseDto> findByPersonId(@PathVariable long personId){
        UserResponseDto foundUser = authenticationService.findByPersonId(personId);
        if (foundUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundUser);      
    }
    
    
}
