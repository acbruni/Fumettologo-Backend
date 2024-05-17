package com.example.fumettologobackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "first_name")
    @NotBlank
    private String firstName;

    @Column(name = "last_name")
    @NotBlank
    private String lastName;

    @Column(name = "email")
    @NaturalId
    @NotBlank
    @Email
    private String email;

    @Column(name = "address")
    @NotBlank
    private String address;

    @Column(name = "phone")
    @NotBlank
    private String phone;

    // relazione bidirezionale uno-a-molti con Order
    @OneToMany(targetEntity = Order.class, mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<Order> orders;

    // relazione bidireazionale uno-a-uno con Cart
    @OneToOne(targetEntity = Cart.class, mappedBy = "user",fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private Cart cart;

}
