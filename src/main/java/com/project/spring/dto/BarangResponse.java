package com.project.spring.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BarangResponse {
    private UUID id;
    private String rfid;
    private String namaBarang;
    private double hargaSatuan;
}
