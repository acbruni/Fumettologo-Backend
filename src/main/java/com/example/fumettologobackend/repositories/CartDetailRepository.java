package com.example.fumettologobackend.repositories;

import com.example.fumettologobackend.entities.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// gestisce l'interazione con la base di dati e
// genera un'implementazione concreta dell'interfaccia
@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail,Integer> {

    // restituisce un CartDetail in base all'id fornito
    CartDetail findById(int id);

    // cerca e restituisce un CartDetail basato sull'id del fumetto e sull'id del carrello
    CartDetail findByComicIdAndCartId(int comicId, int cartId);

}
