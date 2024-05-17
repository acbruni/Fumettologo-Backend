package com.example.fumettologobackend.services;

import com.example.fumettologobackend.entities.Order;
import com.example.fumettologobackend.repositories.OrderRepository;
import com.example.fumettologobackend.support.exceptions.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired // iniezione delle dipendenze
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public Order findOne(int id) throws OrderNotFoundException {
        Order order = this.orderRepository.findById(id); // recupera l'ordine tramite l'id
        if(order == null) {
            throw new OrderNotFoundException();
        }
        return order;
    }

    @Transactional(readOnly = true)
    public List<Order> findAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize); // creazione della pagina
        Page<Order> pagedResult = this.orderRepository.findAllByOrderByCreateTimeDesc(pageable); // per ottenere una pagina di risultati ordinati per data di creazione in ordine decerscente
        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        }
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<Order> findByUser(String email, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize); // creazione della pagina
        Page<Order> pagedResult = this.orderRepository.findByUserEmailOrderByCreateTimeDesc(email, pageable); // pagina dei risultati degli ordini dell'utente specificato ordinati per data di creazione in ordine decrescente
        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        }
        return new ArrayList<>();
    }

}
