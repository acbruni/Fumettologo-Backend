package com.example.fumettologobackend.user;

import com.example.fumettologobackend.support.RegistrationRequest;
import com.example.fumettologobackend.support.exceptions.*;

import java.util.List;

public interface UserServiceInterface {
    List<User> findAll();
    User findByEmail(String email) throws UserNotFoundException;
    User register(RegistrationRequest registrationRequest) throws Exception;

}