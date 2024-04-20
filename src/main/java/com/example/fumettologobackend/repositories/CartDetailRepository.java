package com.example.fumettologobackend.repositories;

import com.example.fumettologobackend.entities.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail,Integer> {
    CartDetail findById(int id);

    CartDetail findBycomicIdAndCartId(int comicId, int cartId);
}
