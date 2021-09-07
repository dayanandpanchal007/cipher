package com.cipher.book.controller;

import com.cipher.book.entity.Book;
import com.cipher.book.service.BookService;
import com.cipher.common.service.CipherService;
import com.cipher.common.service.CipherServiceCBCMode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@RequestMapping("/book")
@RestController
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private CipherServiceCBCMode cipherService;

    @GetMapping
    public ResponseEntity<String> getAllBooks() {
        List<Book> books = bookService.fetchAllBooks();
        try {
            String ciphertext = cipherService.encrypt(new ObjectMapper().writeValueAsString(books));
            return new ResponseEntity<>(ciphertext, HttpStatus.OK);
        } catch (JsonProcessingException ex) {
            return new ResponseEntity<>("Unable to parse", HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<String> saveBook(@RequestBody String req) {
        try {
            String plaintext = cipherService.decrypt(req);
            Book book = new ObjectMapper().readValue(plaintext, Book.class);
            Book savedBook = bookService.saveBook(book);
            String cipherText = cipherService.encrypt(new ObjectMapper().writeValueAsString(savedBook));
            return new ResponseEntity<>(cipherText, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Unable to parse", HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
