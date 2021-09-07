package com.cipher.common.service.deprecated;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

@Service
public class CipherService {

    @Value("${alg.aes.secret}")
    private String SECRET_KEY;

    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static void setKey(String myKey) {
        key = myKey.getBytes(StandardCharsets.UTF_8);
        key = Arrays.copyOf(key, 16);
        secretKey = new SecretKeySpec(key, "AES");
    }

    public Cipher init(int mode) {
        setKey(SECRET_KEY);
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/EBC/PKCS5Padding");
            cipher.init(mode, secretKey);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw e;
        }
        return cipher;
    }

    public String encrypt(String strToEncrypt) {
        try {
            Cipher cipher = init(Cipher.ENCRYPT_MODE);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public String decrypt(String strToDecrypt) {
        try {
            Cipher cipher = init(Cipher.DECRYPT_MODE);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
}
