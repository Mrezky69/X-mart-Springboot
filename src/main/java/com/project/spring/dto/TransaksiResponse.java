package com.project.spring.dto;

import java.util.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransaksiResponse {
    private Long id;
    private String qrCode;
    private String rfid;
    private double hargaSatuan;
    private int jumlah;
    private Date tanggalJam;
    private String nama;
    private double wallet;
}
