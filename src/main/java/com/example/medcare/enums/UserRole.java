package com.example.medcare.enums;

/**
 * Enumeração que representa os papéis (roles) disponíveis para os usuários do sistema.
 *
 * <p>
 * Cada valor do enum define um nível de permissão e função dentro da aplicação,
 * sendo utilizado principalmente pelo Spring Security para controle de acesso.
 * </p>
 *
 * <p><b>Papéis disponíveis:</b></p>
 * <ul>
 *     <li><b>ADMIN</b> — Possui acesso total ao sistema, incluindo funcionalidades administrativas.</li>
 *     <li><b>USER</b> — Papel padrão com acesso restrito a funcionalidades básicas.</li>
 *     <li><b>MEDIC</b> — Usuário com permissões específicas para profissionais da saúde.</li>
 * </ul>
 *
 * <p>
 * Cada enum possui também uma string associada, utilizada em contextos onde a
 * representação textual do papel é necessária (como na geração de tokens JWT).
 * </p>
 *
 * @author 
 */
public enum UserRole {
    /** Papel de administrador, com permissão total. */
    ADMIN("admin"),
    /** Papel padrão de usuário comum. */
    USER("user"),
    /** Papel destinado aos profissionais de saúde. */
    MEDIC("medic");
    /** Representação textual do papel. */
    private final String role;
    /**
     * Construtor do enum.
     *
     * @param role representação textual do papel.
     */
    UserRole(String role){
        this.role = role;
    }
    /**
     * Retorna a representação textual do papel do usuário.
     *
     * @return o nome do papel em formato string.
     */
    public String getRole(){
        return role;
    }
}
