package com.example.medcare.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.medcare.dto.RegisterRequestDTO;
import com.example.medcare.dto.UserResponseDto;
import com.example.medcare.models.User;

/**
 * Mapper responsável pela conversão entre entidades {@link User} e seus respectivos
 * DTOs utilizando a biblioteca <b>MapStruct</b>.
 *
 * <p>
 * Esta interface permite a conversão automática entre objetos, reduzindo a necessidade
 * de código repetitivo (boilerplate) e garantindo padronização entre modelos e DTOs.
 * O MapStruct gera automaticamente a implementação desta interface em tempo de compilação.
 * </p>
 *
 * <p>
 * O componente é registrado como um bean Spring devido ao parâmetro
 * {@code componentModel = "spring"}, permitindo injeção via {@code @Autowired}.
 * </p>
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Converte uma entidade {@link User} para o DTO {@link UserResponseDto}.
     *
     * <p>
     * Esta conversão é utilizada principalmente em respostas de endpoints,
     * como no método <code>GET /me</code>.
     * </p>
     *
     * <p>
     * O campo {@code password} do usuário é automaticamente ignorado pelo MapStruct,
     * pois não existe no DTO — garantindo que a senha nunca seja exposta.
     * </p>
     *
     * @param user a entidade {@link User} a ser convertida.
     * @return o DTO {@link UserResponseDto} contendo os dados públicos do usuário.
     */
    UserResponseDto toUserResponseDTO(User user);

    /**
     * Converte um {@link RegisterRequestDTO} em uma entidade {@link User}.
     *
     * <p>
     * Os campos {@code role} e {@code authorities} são ignorados nesta conversão,
     * pois tais informações devem ser definidas manualmente pela lógica de serviço
     * e não diretamente pelo DTO de registro.
     * </p>
     *
     * @param dto o DTO contendo os dados de entrada para criação de um novo usuário.
     * @return uma nova instância de {@link User} com os dados mapeados.
     */
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "isFirstPassword", ignore = true)
    User toUser(RegisterRequestDTO dto);
}
