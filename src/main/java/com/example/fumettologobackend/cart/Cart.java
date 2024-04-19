package com.example.fumettologobackend.cart;

import com.example.fumettologobackend.cartDetail.CartDetail;
import com.example.fumettologobackend.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "carts")
@Data
public class Cart implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    @ToString.Exclude
    private User user;

    @OneToMany(targetEntity = CartDetail.class, mappedBy = "cart")
    private List<CartDetail> cartDetails;

}
