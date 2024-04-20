package com.example.fumettologobackend.repositories;

import com.example.fumettologobackend.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {
    Order findById(int id);

    Page<Order> findAllByOrderByCreateTimeDesc(Pageable pageable);

    Page<Order> findByUserEmailOrderByCreateTimeDesc(String email, Pageable pageable);

}