package com.durlabh.codes.secure_boot.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="authority")
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;

}
