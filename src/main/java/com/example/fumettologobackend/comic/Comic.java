package com.example.fumettologobackend.comic;

import com.example.fumettologobackend.cartDetail.CartDetail;
import com.example.fumettologobackend.orderDetail.OrderDetail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

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
    @NotBlank
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
    @PositiveOrZero
    private int quantity;

    @Version
    @JsonIgnore
    @ToString.Exclude
    @Column(name = "version")
    private long version;

    @OneToMany(targetEntity = OrderDetail.class, mappedBy = "comic", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<OrderDetail> orderDetails;

    @OneToMany(targetEntity = CartDetail.class, mappedBy = "comic", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<CartDetail> cartDetails;

}
