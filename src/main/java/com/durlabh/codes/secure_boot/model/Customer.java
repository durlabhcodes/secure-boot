package com.durlabh.codes.secure_boot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    private String name;

    private String email;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String pwd;

    private String role;

    @Column(name = "create_dt")
    @JsonIgnore
    private Date createDt;

    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    //@JsonIgnore
    private List<Authority> authorities;
}
