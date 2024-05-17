package com.example.fumettologobackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "carts") // nome tabella database
@Data // autogenera metodi

// serializzazione utile per il salvataggio dello stato dell'oggetti su disco o per trasmissione attraverso una rete
public class Cart implements Serializable {

    @Id // chiave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id generato automaticamente dal database
    @Column(name = "id") // nome colonna id
    private int id;

    @OneToOne(targetEntity = User.class) // relazione uno-a-uno con User
    @JoinColumn(name = "user_id") // chiave esterna
    @JsonIgnore // ignorata durante la serializzazione json
    @ToString.Exclude
    private User user;

    @OneToMany(targetEntity = CartDetail.class, mappedBy = "cart") // relazione bidirezionale molti-a-uno con CartDetail
    private List<CartDetail> cartDetails;

}
