package com.project.spring.model;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "barang")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Barang {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "rfid", nullable = false, unique = true)
    private String rfid;

    @Column(name = "nama_barang", nullable = false)
    private String namaBarang;

    @Column(name = "harga_satuan", nullable = false)
    private double hargaSatuan;

}
