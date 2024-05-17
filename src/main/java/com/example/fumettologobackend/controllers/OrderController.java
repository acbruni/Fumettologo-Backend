package com.example.fumettologobackend.controllers;

import com.example.fumettologobackend.entities.Order;
import com.example.fumettologobackend.entities.OrderDetail;
import com.example.fumettologobackend.services.OrderService;
import com.example.fumettologobackend.support.authentication.JwtUtils;
import com.example.fumettologobackend.support.exceptions.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:9090", allowedHeaders = "*")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/admin/orders")
    public ResponseEntity<?> getOrders(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                       @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        // recupera tutti gli ordini
        List<Order> orders = orderService.findAll(pageNumber, pageSize);
        if(orders.isEmpty()) {
            return new ResponseEntity<>("No orders found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/profile/orders")
    public ResponseEntity<?> getUserOrders(Authentication authentication,
                                           @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
                                           @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        // recupera tutti gli ordini di un utente specifico
        String email = JwtUtils.getEmailFromAuthentication(authentication);
        List<Order> orders = orderService.findByUser(email, pageNumber, pageSize);
        if(orders.isEmpty()) {
            return new ResponseEntity<>("No orders found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/profile/orders/{id}")
    public ResponseEntity<?> getOrderDetails(@PathVariable("id") int id, Authentication authentication) {
        try {
            // recupera i dettagli di un ordine specifico
            String email = JwtUtils.getEmailFromAuthentication(authentication);
            Order order = this.orderService.findOne(id);
            // HTTP 401 se l'ordine non appartiene all'utente autenticato
            if(!order.getUser().getEmail().equals(email)) {
                return new ResponseEntity<>("Unauthorized access", HttpStatus.UNAUTHORIZED);
            }
            List<OrderDetail> od = order.getOrderDetails();
            return new ResponseEntity<>(od, HttpStatus.OK);
        } catch (OrderNotFoundException e) {
            return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
        }
    }

}
