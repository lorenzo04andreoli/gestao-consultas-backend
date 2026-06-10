package com.lorenzo.gestaoconsultas.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@Converter
public class CryptoConverter implements AttributeConverter<String, String> {

    private static final String PREFIXO = "enc:v1:";
    private static final String ALGORITMO = "AES/GCM/NoPadding";
    private static final int IV_BYTES = 12;
    private static final int TAG_BITS = 128;
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public String convertToDatabaseColumn(String valor) {
        if (valor == null || valor.isBlank() || valor.startsWith(PREFIXO)) {
            return valor;
        }

        try {
            byte[] iv = new byte[IV_BYTES];
            RANDOM.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.ENCRYPT_MODE, chave(), new GCMParameterSpec(TAG_BITS, iv));

            byte[] criptografado = cipher.doFinal(valor.getBytes(StandardCharsets.UTF_8));

            return PREFIXO
                    + Base64.getEncoder().encodeToString(iv)
                    + ":"
                    + Base64.getEncoder().encodeToString(criptografado);
        } catch (Exception ex) {
            throw new IllegalStateException("Erro ao criptografar dado sensivel", ex);
        }
    }

    @Override
    public String convertToEntityAttribute(String valorBanco) {
        if (valorBanco == null || valorBanco.isBlank() || !valorBanco.startsWith(PREFIXO)) {
            return valorBanco;
        }

        try {
            String conteudo = valorBanco.substring(PREFIXO.length());
            String[] partes = conteudo.split(":", 2);

            byte[] iv = Base64.getDecoder().decode(partes[0]);
            byte[] criptografado = Base64.getDecoder().decode(partes[1]);

            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.DECRYPT_MODE, chave(), new GCMParameterSpec(TAG_BITS, iv));

            return new String(cipher.doFinal(criptografado), StandardCharsets.UTF_8);
        } catch (Exception ex) {
            return null;
        }
    }

    private SecretKeySpec chave() {
        try {
            String segredo = System.getenv("APP_CRYPTO_SECRET_KEY");

            if (segredo == null || segredo.isBlank()) {
                segredo = "dentix-dev-secret-key-change-me";
            }

            byte[] hash = MessageDigest.getInstance("SHA-256")
                    .digest(segredo.getBytes(StandardCharsets.UTF_8));

            return new SecretKeySpec(hash, "AES");
        } catch (Exception ex) {
            throw new IllegalStateException("Erro ao preparar chave criptografica", ex);
        }
    }
}
