package com.example.fumettologobackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Table(name = "cart_details")
@Data
@NoArgsConstructor
public class CartDetail implements Serializable {

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

    @ManyToOne(targetEntity = Cart.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "cart")
    @JsonIgnore
    @ToString.Exclude
    private Cart cart;

    @Column(name = "subtotal")
    @NotNull
    @Positive
    private float subTotal;

    @Version
    @JsonIgnore
    @ToString.Exclude
    @Column(name = "version")
    private long version;

    public CartDetail(Cart cart, Comic comic) {
        this.cart = cart;
        this.comic = comic;
        this.price = comic.getPrice();
        this.quantity = 1;
        this.subTotal = comic.getPrice();
    }

}
