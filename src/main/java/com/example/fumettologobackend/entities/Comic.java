package com.example.fumettologobackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "comic")
@Data
public class Comic implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    @NotBlank // non può essere vuoto
    private String title;

    @Column(name = "author")
    @NotBlank
    private String author;

    @Column(name = "publisher")
    @NotBlank
    private String publisher;

    @Column(name = "category")
    @NotBlank
    private String category;

    @Column(name = "price")
    @NotNull
    @Positive
    private float price;

    @Column(name = "quantity")
    @NotNull
    @PositiveOrZero // positivo o zero
    private int quantity;

    @Version
    @JsonIgnore
    @ToString.Exclude
    @Column(name = "version")
    private long version;

    @Column(name = "image")
    @NotNull
    private String image;

    // relazione bidirezionale uno-a-molti con OrderDetail
    // FetchType.LAZY indica che orderDetails non verrà caricata nel database quando viene
    // caricata un'istanza di Comic, ma solo quando il codice tenta di accedere a questa lista
    // ottimizza l'accesso ai dati
    @OneToMany(targetEntity = OrderDetail.class, mappedBy = "comic", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<OrderDetail> orderDetails;

    @OneToMany(targetEntity = CartDetail.class, mappedBy = "comic", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<CartDetail> cartDetails;

}
