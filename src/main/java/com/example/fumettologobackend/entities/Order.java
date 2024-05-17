package com.example.fumettologobackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor // genera un costruttore senza argomenti
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @CreationTimestamp // il campo createTime verrà rimpeito con il timestamp corrente alla creazione dell'entità
    @Temporal(TemporalType.TIMESTAMP) // specifica il timestamp da utilizzare nel database
    @Column(name = "create_time")
    private LocalDateTime createTime;

    @ManyToOne(targetEntity = User.class) // relazione molti-a-uno con User
    @JoinColumn(name = "user_id")
    @JsonIgnore
    @ToString.Exclude
    private User user;

    @Column(name = "total")
    @NotNull
    @PositiveOrZero
    private float total;

    // relazione bidirezionale uno-a-molti con OrderDetail
    @OneToMany(targetEntity = OrderDetail.class, mappedBy = "order", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<OrderDetail> orderDetails;

    public Order(User user) {
        this.user = user;
    }

}


