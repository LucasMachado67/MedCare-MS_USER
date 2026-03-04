package com.example.medcare.services;

import com.example.medcare.enums.UserRole;
import org.springframework.context.ApplicationContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.medcare.dto.AuthenticationDTO;
import com.example.medcare.dto.LoginResponseDTO;
import com.example.medcare.dto.RegisterRequestDTO;
import com.example.medcare.dto.UserResponseDto;
import com.example.medcare.exceptions.UsernameAlreadyExistsException;
import com.example.medcare.mappers.UserMapper;
import com.example.medcare.models.User;
import com.example.medcare.producer.UserProducer;
import com.example.medcare.repositories.UserRepository;
import com.example.medcare.security.TokenService;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.validation.Valid;

/**
 * Serviço responsável pela autenticação, registry e gestão de usuários.
 *
 * <p>
 * Esta classe integra com o Spring Security implementando
 * {@link UserDetailsService},
 * permitindo que o sistema valide credenciais e carregue informações do usuário
 * durante
 * o processo de autenticação.
 * </p>
 *
 * <p>
 * Também é responsável pela criação de credenciais, geração de ‘tokens’ JWT e
 * envio de
 * notificações através do serviço de mensageria (AmazonSQS).
 * </p>
 */
@Service
public class UserAuthenticationService implements UserDetailsService {

    // Injeção do repository para acesso ao banco de dados
    @Autowired
    private UserRepository userRepository;

    // Injeção do service JWT para geração de ‘tokens’
    @Autowired
    private TokenService tokenService;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserProducer userProducer;

    @Autowired
    private UserMapper mapper;

    /**
     * Cria credenciais de acesso para uma pessoa existente no sistema.
     *
     * <p>
     * Este método é utilizado quando o módulo de entidade (Person/Medic) solicita
     * a criação de um usuário baseado em um personId.
     * </p>
     *
     * <p>
     * O processo inclui:
     * </p>
     * <ul>
     * <li>Gerar uma senha inicial segura.</li>
     * <li>Criar e salvar a entidade {@link User}.</li>
     * <li>Cryptographic da senha gerada.</li>
     * <li>Definir a role informada.</li>
     * <li>Enviar um e-mail ao usuário com as suas credenciais iniciais.</li>
     * </ul>
     *
     * @param personId 'ID' da pessoa vinculada ao usuário.
     * @param email    e-mail que será usado como username para 'login'.
     * @param role     papel do usuário no sistema (ex.: ADMIN, MEDIC, PATIENT).
     */
    public void createUserCredentials(Long personId, String email, String role) throws JsonProcessingException {

        if (userRepository.findByPersonId(personId) != null) {
            System.out.println("Usuário já existe para personId=" + personId);
            return;
        }
        // 1. Gerar senha inicial segura (começo do e-mail + 123), depois será solicitado para troca de senha
        String initialPassword = email.split("@")[0] + 123;

        // 2. Criar e Salvar a Entidade User
        User newUser = new User();
        newUser.setPersonId(personId);
        newUser.setEmail(email);
        // Criptografe a senha antes de salvar
        newUser.setPassword(passwordEncoder.encode(initialPassword));
        // Defina a role (com o prefixo ROLE_ se necessário)
        // Ex: "MEDIC" -> "ROLE_MEDIC"
        UserRole roleToSave = UserRole.valueOf(role);
        newUser.setRole(roleToSave);

        userRepository.save(newUser);

        // 3. Enviar e-mail para o usuário com a senha inicial.
        userProducer.publishMessageEmail(newUser, initialPassword);
    }

    /**
     * Carrega um usuário pelo username.
     *
     * <p>
     * Este método é usado internamente pelo Spring Security durante o processo
     * de autenticação.
     * </p>
     *
     * @param email nome de usuário (e-mail) utilizado para ‘login’.
     * @return detalhes do usuário authenticated.
     * @throws UsernameNotFoundException se o usuário não existir.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
        return user;
    }

    /**
     * Realiza autenticação e gera um ‘token’ JWT para o usuário.
     *
     * <p>
     * Processo:
     * </p>
     * <ul>
     * <li>Autentica o usuário utilizando AuthenticationManager.</li>
     * <li>Valida credenciais.</li>
     * <li>Gera token JWT.</li>
     * <li>Retorna informações úteis ao frontend.</li>
     * </ul>
     *
     * @param data DTO contendo username e senha.
     * @return LoginResponseDTO contendo token, username, personId e role.
     * @throws BadCredentialsException se as credenciais forem inválidas.
     */
    public LoginResponseDTO login(@RequestBody @Valid AuthenticationDTO data) {
        try {
            AuthenticationManager authenticationManager = context.getBean(AuthenticationManager.class);
            // Cria o ‘token’ de autenticação sem estar autenticado
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
            // Autentica o usuário. Se falhar (senha incorreta), lança uma exceção pelo
            // Spring Security.
            var auth = authenticationManager.authenticate(usernamePassword);
            // Pega o objeto ‘User’ autenticado
            var user = (User) auth.getPrincipal();
            // Gera o token JWT
            var token = tokenService.generateToken(user);

            // Retorna o DTO de resposta
            return new LoginResponseDTO(
                    token,
                    user.getUsername(),
                    user.getPersonId(),
                    user.getRole(),
                    user.getTenantId(),
                    user.IsFirstPassword());
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Credenciais inválidas: " + e.getLocalizedMessage());
        }
    }

    /**
     * Registry um novo usuário no sistema.
     *
     * <p>
     * Processo:
     * </p>
     * <ul>
     * <li>Valida se o username já está a ser utilizado.</li>
     * <li>Criptografa a senha informada.</li>
     * <li>Cria e salva a entidade 'User'.</li>
     * </ul>
     *
     * @param registerDTO informações do novo usuário.
     * @throws UsernameAlreadyExistsException se o username já existir.
     */
    public void register(RegisterRequestDTO registerDTO, String tenantId) {

        try {
            // Validação: Verifica se o usuário já existe
            if (this.userRepository.findByEmail(registerDTO.getEmail()) != null) {
                throw new UsernameAlreadyExistsException(registerDTO.getEmail());
            }
            // Criptografia a senha antes de salvar no banco de dados
            String encryptedPassword = new BCryptPasswordEncoder().encode(registerDTO.getPassword());

            // Cria a nova entidade ‘User’
            User newUser = new User();
            newUser.setEmail(registerDTO.getEmail());
            newUser.setPassword(encryptedPassword);
            newUser.setPersonId(registerDTO.getPersonId());
            newUser.setRole(registerDTO.getUserRole());
            newUser.setTenantId(tenantId);
            // Salva o novo usuário
            userRepository.save(newUser);
        } catch (UsernameAlreadyExistsException e) {
            throw new UsernameAlreadyExistsException("Nome de usuário já existente" + e.getMessage());
        }

    }

    /**
     * Recupera o usuário autenticado do contexto do Spring Security.
     *
     * <p>
     * Este método é útil em rotas protegidas em que é necessário saber
     * quem está realizando a requisição.
     * </p>
     *
     * @return o usuário autenticado.
     * @throws AccessDeniedException se não houver usuário autenticado.
     */
    public User getAuthenticatedUser() {
        try {
            // Pega a autenticação do contexto de segurança
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            // Verifica se o usuário está autenticado
            if (auth == null || !auth.isAuthenticated()) {
                throw new RuntimeException("User not authenticated");
            }
            // Retorna o objeto principal (a Entidade ‘User’)
            return (User) auth.getPrincipal();
        } catch (AccessDeniedException e) {
            throw new AccessDeniedException("403 - Forbidden, necessáro efetuar login", e);
        }
    }

    /**
     * Recupera um usuário associado a um determinado personId.
     *
     * @param id identificador da entidade Person.
     * @return DTO contendo informações limitadas do usuário.
     */
    public UserResponseDto findByPersonId(Long id) {
        User foundUser = userRepository.findByPersonId(id);

        return mapper.toUserResponseDTO(foundUser);
    }

    public void updatePassword(String newPassword){
        // 1. Pega o "Principal" do contexto.
        // O erro diz que isso aqui está vindo como String!
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String email;
        if (principal instanceof UserDetails userDetails) {
            email = userDetails.getUsername();
        } else {
            email = principal.toString();
        }
        System.out.println("Buscando usuário com o identificador: [" + email + "]");
        // 2. Busca o usuário real no banco pelo e-mail
        User user = userRepository.findByEmail(email);

        // 3. Atualiza a senha (criptografada) e a flag
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        user.setIsFirstPassword(false);

        userRepository.save(user);
    }
}
