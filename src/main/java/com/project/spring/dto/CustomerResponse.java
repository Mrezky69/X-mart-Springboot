package com.project.spring.dto;

import java.util.UUID;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {
    private UUID id;
    private String qrCode;
    private String nama;
    private double wallet;
}
