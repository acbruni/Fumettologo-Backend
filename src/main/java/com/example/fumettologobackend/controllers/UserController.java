package com.example.fumettologobackend.controllers;

import com.example.fumettologobackend.entities.User;
import com.example.fumettologobackend.services.UserService;
import com.example.fumettologobackend.support.RegistrationRequest;
import com.example.fumettologobackend.support.authentication.JwtUtils;
import com.example.fumettologobackend.support.exceptions.KeycloackRegistrationException;
import com.example.fumettologobackend.support.exceptions.MailUserAlreadyExistsException;
import com.example.fumettologobackend.support.exceptions.UserNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:9090", allowedHeaders = "*")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/users")
    public List<User> getAll() {
        return this.userService.findAll();
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        try {
            // recupera il profilo dell'utente
            String email = JwtUtils.getEmailFromAuthentication(authentication);
            User user = this.userService.findByEmail(email);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest registrationRequest) {
        try {
            // registra un nuovo utente
            User savedUser = this.userService.register(registrationRequest);
            // restituisce l'utente creato con stato HTTP 201
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (MailUserAlreadyExistsException e) {
            // HTTP 409 se l'email esiste già
            return new ResponseEntity<>("Email already exists", HttpStatus.CONFLICT);
        } catch (KeycloackRegistrationException e) {
            // HTTP 400 se c'è un errore nella registrazione con keycloak
            return new ResponseEntity<>("Error in registration", HttpStatus.BAD_REQUEST);
        }
    }

}