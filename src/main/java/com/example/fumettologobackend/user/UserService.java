package com.example.fumettologobackend.user;

import com.example.fumettologobackend.cart.Cart;
import com.example.fumettologobackend.cart.CartRepository;
import com.example.fumettologobackend.support.Registration;
import com.example.fumettologobackend.support.RegistrationRequest;
import com.example.fumettologobackend.support.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements UserServiceInterface{
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    @Autowired
    public UserService(UserRepository userRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String email) throws UserNotFoundException {
        if(!this.userRepository.existsByEmail(email)) {
            throw new UserNotFoundException();
        }
        return this.userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public User register(RegistrationRequest registrationRequest) throws MailUserAlreadyExistsException,
                                                                         KeycloackRegistrationException {
        User user = registrationRequest.getUser();
        if(this.userRepository.existsByEmail(user.getEmail())) {
            throw new MailUserAlreadyExistsException();
        }
        try {
            Registration.keycloackRegistration(registrationRequest);
        } catch (KeycloackRegistrationException ke) {
            throw new KeycloackRegistrationException();
        }
        User savedUser = this.userRepository.save(user);
        Cart userCart = new Cart();
        userCart.setUser(savedUser);
        savedUser.setCart(this.cartRepository.save(userCart));
        return savedUser;
    }

}
