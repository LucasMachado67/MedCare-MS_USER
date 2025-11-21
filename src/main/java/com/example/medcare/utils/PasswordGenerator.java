package com.example.medcare.utils;

import java.security.SecureRandom;

/**
 * Classe utilitária responsável pela geração de senhas aleatórias.
 *
 * <p>Utiliza {@link SecureRandom} para garantir uma geração segura e
 * criptograficamente forte. As senhas geradas possuem tamanho fixo de 10
 * caracteres alfanuméricos (maiúsculos, minúsculos e dígitos).</p>
 *
 * <p>Este utilitário é ideal para geração de senhas temporárias em fluxos
 * de recuperação de senha, criação inicial de contas ou códigos provisórios.</p>
 */
public class PasswordGenerator {
    /**
     * Conjunto de caracteres permitido para composição da senha.
     * Inclui letras maiúsculas, minúsculas e dígitos.
     */
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    /**
     * Tamanho padrão da senha gerada.
     */
    private static final int PASSWORD_LENGTH = 10;
     /**
     * Gera uma senha aleatória utilizando {@link SecureRandom} para garantir
     * imprevisibilidade e segurança.
     *
     * <p>A senha resultante possui sempre 10 caracteres, com valores
     * escolhidos aleatoriamente do conjunto definido em {@link #CHARACTERS}.</p>
     *
     * @return Uma string contendo a senha gerada.
     */
    public static String generate(){
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(PASSWORD_LENGTH);
        for(int i = 0; i < PASSWORD_LENGTH; i++){
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}
