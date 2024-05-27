package com.project.spring.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.project.spring.dto.*;
import com.project.spring.model.*;
import com.project.spring.repository.*;

@Service
public class BarangService {

    @Autowired
    private BarangRepository barangRepository;

    public List<BarangResponse> listBarang() {
        List<Barang> barang = barangRepository.findAll();
        List<BarangResponse> responses = new ArrayList<>();
        for (Barang data : barang) {
            responses.add(BarangResponse.builder().id(data.getId()).namaBarang(data.getNamaBarang()).rfid(data.getRfid()).hargaSatuan(data.getHargaSatuan()).build());
        }
        return responses;
    }

    public BarangResponse getBarangById(String rfid) {
        Barang existingBarang = barangRepository.findByRfid(rfid);
        return BarangResponse.builder()
                .id(existingBarang.getId())
                .namaBarang(existingBarang.getNamaBarang())
                .rfid(existingBarang.getRfid())
                .hargaSatuan(existingBarang.getHargaSatuan())
                .build();
    }

    public BarangResponse updateBarang(UUID id, BarangRequest barang) {
        try {
            Barang existingBarang = barangRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Barang not found"));

            existingBarang.setNamaBarang(barang.getNama());
            existingBarang.setRfid(existingBarang.getRfid());
            existingBarang.setHargaSatuan(barang.getHarga());

            Barang updatedBarang = barangRepository.save(existingBarang);

            return BarangResponse.builder()
                    .id(updatedBarang.getId())
                    .namaBarang(updatedBarang.getNamaBarang())
                    .rfid(updatedBarang.getRfid())
                    .hargaSatuan(updatedBarang.getHargaSatuan())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void deleteBarang(UUID id) {
        try {
            Barang existingBarang = barangRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Barang not found"));
            barangRepository.delete(existingBarang);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
        public BarangResponse add(BarangRequest barang) {
        try {
            Barang existingBarang = barangRepository.findByNamaBarang(barang.getNama());
            if (existingBarang != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data already exists");
            }

            if (barang.getNama() == null || barang.getNama().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nama barang tidak boleh kosong");
            }

            Barang newbBarang = new Barang();
            newbBarang.setNamaBarang(barang.getNama());
            newbBarang.setRfid(UUID.randomUUID().toString());
            newbBarang.setHargaSatuan(barang.getHarga());
            barangRepository.save(newbBarang);
            return BarangResponse.builder()
                    .id(newbBarang.getId())
                    .rfid(newbBarang.getRfid())
                    .namaBarang(newbBarang.getNamaBarang())
                    .hargaSatuan(newbBarang.getHargaSatuan())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
