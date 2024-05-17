package com.example.fumettologobackend.repositories;

import com.example.fumettologobackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    // verifica se esiste un utente con l'email specificata nel db
    boolean existsByEmail(String email);

    // cerca e restituisce un'istanza di User basata sull'email fonita
    User findByEmail(String email);

}
