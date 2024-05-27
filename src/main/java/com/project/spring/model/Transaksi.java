package com.project.spring.model;

import java.util.*;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "transaksi")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaksi {    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "qrcode", nullable = false)
    private String qrCode;

    @Column(name = "rfid", nullable = false)
    private String rfid;

    @Column(name = "harga_satuan", nullable = false)
    private double hargaSatuan;

    @Column(name = "jumlah", nullable = false)
    private int jumlah;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "tanggal_jam", nullable = false)
    private Date tanggalJam;

}
