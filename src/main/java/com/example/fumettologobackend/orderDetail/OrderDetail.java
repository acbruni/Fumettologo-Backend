package com.example.fumettologobackend.orderDetail;

import com.example.fumettologobackend.comic.Comic;
import com.example.fumettologobackend.order.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Table(name = "order_details")
@Data
@NoArgsConstructor
public class OrderDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(targetEntity = Comic.class)
    @JoinColumn(name = "comic")
    private Comic comic;

    @Column(name = "price")
    @NotNull
    private float price;

    @Column(name = "quantity")
    @Positive
    @NotNull
    private int quantity;

    @ManyToOne(targetEntity = Order.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase")
    @JsonIgnore
    @ToString.Exclude
    private Order order;

    @Column(name = "subtotal")
    @NotNull
    @Positive
    private float subTotal;

    public OrderDetail(Comic comic, Order order, float price, int quantity) {
        this.comic = comic;
        this.order = order;
        this.price = price;
        this.quantity = quantity;
        this.subTotal = this.price*this.quantity;
    }

}
