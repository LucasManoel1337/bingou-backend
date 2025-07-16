package project.service.security;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public final class CripDescripSenhaService {

    private static final String CHAVE = "MinhaChave123456"; // 16 caracteres (128 bits)

    public static String criptografar(String senha) throws Exception {
        SecretKeySpec key = new SecretKeySpec(CHAVE.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] criptografado = cipher.doFinal(senha.getBytes());
        return Base64.getEncoder().encodeToString(criptografado);
    }

    public static String descriptografar(String senhaCriptografada) throws Exception {
        SecretKeySpec key = new SecretKeySpec(CHAVE.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodificado = Base64.getDecoder().decode(senhaCriptografada);
        byte[] senha = cipher.doFinal(decodificado);
        return new String(senha);
    }

}
