package com.cipher.common.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Service
public class CipherServiceCBCMode {

    private final String algorithm = "AES/CBC/PKCS5Padding";

    @Value("${alg.aes.secret}")
    private String SECRET_KEY;

    @Value("${alg.aes.salt}")
    private String SALT;

    private final int keyLength = 256;
    private final int iterationCount = keyLength * keyLength; // it can be anything

    public SecretKey generateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        // arguments -> key, salt, iterationCount and keyLength
        KeySpec keySpec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), iterationCount, keyLength);
        SecretKey secretKey = factory.generateSecret(keySpec);
        return new SecretKeySpec(secretKey.getEncoded(), "AES");
    }

    public IvParameterSpec generateIv() {
        byte[] iv = {1, 0, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0};
        return new IvParameterSpec(iv);
    }

    public Cipher getCipherInstance(int operationMode) throws NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidAlgorithmParameterException,
            InvalidKeyException,
            InvalidKeySpecException {
        SecretKey key = generateKey();
        IvParameterSpec iv = generateIv();
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(operationMode, key, iv);
        return cipher;
    }

    public String encrypt(String input) throws NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidAlgorithmParameterException,
            InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException,
            InvalidKeySpecException {
        Cipher cipher = getCipherInstance(Cipher.ENCRYPT_MODE);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder()
                .encodeToString(cipherText);
    }

    public String decrypt(String cipherText) throws InvalidAlgorithmParameterException,
            NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidKeySpecException,
            InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException {
        Cipher cipher = getCipherInstance(Cipher.DECRYPT_MODE);
        byte[] plainText = cipher.doFinal(Base64.getDecoder()
                .decode(cipherText));
        return new String(plainText);
    }
}
