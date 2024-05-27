package com.project.spring.model;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "qrcode", nullable = false, unique = true)
    private String qrCode;

    @Column(name = "nama", nullable = false)
    private String nama;

    @Column(name = "wallet", nullable = false)
    private double wallet;

}
