package com.cipher.book.controller;

import com.cipher.common.service.CipherServiceCBCMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/aes")
@RestController
public class TestController {

    @Autowired
    private CipherServiceCBCMode cipherService;

    @GetMapping("/encryption/{text}")
    public ResponseEntity<String> encryptData(@PathVariable String text) {
        try {
            return new ResponseEntity<>(cipherService.encrypt(text), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Internal Server Error", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/decryption/{cipherText}")
    public ResponseEntity<String> decryptData(@PathVariable String cipherText) {
        try {
            return new ResponseEntity<>(cipherService.decrypt(cipherText), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Internal Server Error", HttpStatus.BAD_REQUEST);
        }
    }
}
