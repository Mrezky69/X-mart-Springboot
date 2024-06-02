package com.project.spring.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BarangResponse {
    private UUID id;
    private String rfid;
    private String namaBarang;
    private double hargaSatuan;
}
