package com.example.medcare.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.medcare.models.User;

import jakarta.transaction.Transactional;

/**
 * Repositório responsável pelo acesso e manipulação dos dados da entidade {@link User}.
 *
 * <p>Esta interface estende {@link JpaRepository}, fornecendo métodos prontos para
 * operações CRUD e consultas adicionais relacionadas às credenciais de usuário.</p>
 *
 * <p>O repositório implementa consultas personalizadas tanto por convenção de nomes
 * quanto por query nativa.</p>
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

     /**
     * Busca um usuário pelo seu nome de usuário (Email).
     *
     * <p>Este método utiliza a convenção de nomes do Spring Data JPA para gerar
     * automaticamente a consulta.</p>
     *
     * @param username nome de usuário a ser pesquisado
     * @return o usuário encontrado ou {@code null} caso não exista
     */
    User findByUsername(String username);

     /**
     * Atualiza a senha de um usuário com base no seu nome de usuário.
     *
     * <p>Esta operação é executada por meio de uma query nativa, utilizando
     * {@link Modifying} e {@link Transactional} para permitir a modificação direta 
     * no banco de dados.</p>
     *
     * @param password nova senha já criptografada a ser salva
     * @param username nome de usuário cujo registro terá a senha atualizada
     */
    @Transactional
    @Modifying
    @Query(value = """
               UPDATE users
               SET password = :password
               WHERE username = :username
            """, nativeQuery = true)
    void changePassword (@Param("password") String password,@Param("username") String username);

    User findByPersonId(long id);
}
