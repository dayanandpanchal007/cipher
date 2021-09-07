package com.cipher.book.controller;

import com.cipher.common.service.CipherService;
import com.cipher.common.service.CipherServiceCBCMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


@RequestMapping("/aes")
@RestController
public class TestController {

    @Autowired
    private CipherServiceCBCMode cipherService;

    @GetMapping("/encryption/{text}")
    public String encryptData(@PathVariable String text) {
        try {
            return cipherService.encrypt(text);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @GetMapping("/decryption/{cipherText}")
    public String decryptData(@PathVariable String cipherText) {
        try {
            return cipherService.decrypt(cipherText);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
