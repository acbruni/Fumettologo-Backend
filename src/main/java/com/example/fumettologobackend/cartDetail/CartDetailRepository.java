package com.example.fumettologobackend.cartDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail,Integer> {
    CartDetail findById(int id);

    CartDetail findBycomicIdAndCartId(int comicId, int cartId);
}
