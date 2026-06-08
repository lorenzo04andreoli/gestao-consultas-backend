package com.lorenzo.gestaoconsultas.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lorenzo.gestaoconsultas.dto.TwoFactorSetupResponseDto;
import com.lorenzo.gestaoconsultas.entity.Usuario;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;

@Service
public class TwoFactorService {

    private static final String ISSUER = "Dentix";
    private static final String BASE32_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
    private static final int CODE_DIGITS = 6;
    private static final int PERIOD_SECONDS = 30;
    private static final SecureRandom RANDOM = new SecureRandom();

    public TwoFactorSetupResponseDto gerarConfiguracao(Usuario usuario) {
        String secret = gerarSecret();
        String uri = gerarUri(usuario.getEmail(), secret);

        return new TwoFactorSetupResponseDto(secret, gerarQrCode(uri));
    }

    public boolean codigoValido(String secret, String codigo) {
        if (secret == null || codigo == null || !codigo.matches("\\d{6}")) {
            return false;
        }

        long contador = Instant.now().getEpochSecond() / PERIOD_SECONDS;

        for (long janela = -1; janela <= 1; janela++) {
            if (gerarCodigo(secret, contador + janela).equals(codigo)) {
                return true;
            }
        }

        return false;
    }

    private String gerarSecret() {
        byte[] bytes = new byte[20];
        RANDOM.nextBytes(bytes);
        return encodeBase32(bytes);
    }

    private String gerarUri(String email, String secret) {
        String label = ISSUER + ":" + email;

        return "otpauth://totp/"
                + encodeUrl(label)
                + "?secret=" + secret
                + "&issuer=" + encodeUrl(ISSUER)
                + "&algorithm=SHA1"
                + "&digits=" + CODE_DIGITS
                + "&period=" + PERIOD_SECONDS;
    }

    private String gerarQrCode(String conteudo) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(conteudo, BarcodeFormat.QR_CODE, 260, 260);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", output);
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(output.toByteArray());
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao gerar QR Code");
        }
    }

    private String gerarCodigo(String secret, long contador) {
        try {
            byte[] chave = decodeBase32(secret);
            byte[] mensagem = longParaBytes(contador);

            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(chave, "HmacSHA1"));
            byte[] hash = mac.doFinal(mensagem);

            int offset = hash[hash.length - 1] & 0x0f;
            int binario = ((hash[offset] & 0x7f) << 24)
                    | ((hash[offset + 1] & 0xff) << 16)
                    | ((hash[offset + 2] & 0xff) << 8)
                    | (hash[offset + 3] & 0xff);

            int codigo = binario % 1_000_000;
            return String.format("%06d", codigo);
        } catch (Exception ex) {
            return "";
        }
    }

    private byte[] longParaBytes(long valor) {
        byte[] bytes = new byte[8];

        for (int i = 7; i >= 0; i--) {
            bytes[i] = (byte) (valor & 0xff);
            valor >>= 8;
        }

        return bytes;
    }

    private String encodeBase32(byte[] dados) {
        StringBuilder resultado = new StringBuilder();
        int buffer = 0;
        int bitsRestantes = 0;

        for (byte dado : dados) {
            buffer = (buffer << 8) | (dado & 0xff);
            bitsRestantes += 8;

            while (bitsRestantes >= 5) {
                resultado.append(BASE32_ALPHABET.charAt((buffer >> (bitsRestantes - 5)) & 31));
                bitsRestantes -= 5;
            }
        }

        if (bitsRestantes > 0) {
            resultado.append(BASE32_ALPHABET.charAt((buffer << (5 - bitsRestantes)) & 31));
        }

        return resultado.toString();
    }

    private byte[] decodeBase32(String valor) {
        String normalizado = valor.replace("=", "").replace(" ", "").toUpperCase();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int buffer = 0;
        int bitsRestantes = 0;

        for (char caractere : normalizado.toCharArray()) {
            int index = BASE32_ALPHABET.indexOf(caractere);

            if (index < 0) continue;

            buffer = (buffer << 5) | index;
            bitsRestantes += 5;

            if (bitsRestantes >= 8) {
                output.write((buffer >> (bitsRestantes - 8)) & 0xff);
                bitsRestantes -= 8;
            }
        }

        return Arrays.copyOf(output.toByteArray(), output.size());
    }

    private String encodeUrl(String valor) {
        return URLEncoder.encode(valor, StandardCharsets.UTF_8).replace("+", "%20");
    }
}
