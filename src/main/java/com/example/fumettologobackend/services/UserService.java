package com.example.fumettologobackend.services;

import com.example.fumettologobackend.entities.Cart;
import com.example.fumettologobackend.repositories.CartRepository;
import com.example.fumettologobackend.entities.User;
import com.example.fumettologobackend.repositories.UserRepository;
import com.example.fumettologobackend.support.Registration;
import com.example.fumettologobackend.support.RegistrationRequest;
import com.example.fumettologobackend.support.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    @Autowired
    public UserService(UserRepository userRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return this.userRepository.findAll();
    } // restituisce una lista di tutti gli utenti presenti nel sistema

    @Transactional(readOnly = true)
    public User findByEmail(String email) throws UserNotFoundException {
        // se l'utente recuperato tramite l'email fornita esiste, restituiscilo
        if(!this.userRepository.existsByEmail(email)) {
            throw new UserNotFoundException();
        }
        return this.userRepository.findByEmail(email);
    }

    @Transactional
    public User register(RegistrationRequest registrationRequest) throws MailUserAlreadyExistsException,
                                                                         KeycloackRegistrationException {
        User user = registrationRequest.getUser(); // recupera l'oggetto con le informazioni necessarie per la registrazione dell'utente

        // se non esiste si chiama un metodo esterno per registrare l'utente tramite keycloak
        if(this.userRepository.existsByEmail(user.getEmail())) {
            throw new MailUserAlreadyExistsException();
        }
        try {
            Registration.keycloackRegistration(registrationRequest);
        } catch (KeycloackRegistrationException ke) {
            throw new KeycloackRegistrationException();
        }

        User savedUser = this.userRepository.save(user); // l'utente viene salvato nella repository
        Cart userCart = new Cart(); // viene creato un nuovo carrello per l'utente
        userCart.setUser(savedUser);
        savedUser.setCart(this.cartRepository.save(userCart));
        return savedUser;
    }

}
