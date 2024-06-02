package com.project.spring.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.project.spring.dto.*;
import com.project.spring.model.*;
import com.project.spring.repository.*;
import static com.project.spring.util.ResponseMessage.*;

@Service
public class BarangService {

    private final BarangRepository barangRepository;

    @Autowired
    public BarangService(BarangRepository barangRepository) {
        this.barangRepository = barangRepository;
    }

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
            Barang existingBarang = barangRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND));

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
    }

    public void deleteBarang(UUID id) {
            Barang existingBarang = barangRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND));
            barangRepository.delete(existingBarang);
    }
    
        public BarangResponse add(BarangRequest barang) {
            Barang existingBarang = barangRepository.findByNamaBarang(barang.getNama());
            if (existingBarang != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, DATA_ALREADY);
            }

            if (barang.getNama() == null || barang.getNama().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, FIELD_MESSAGE);
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
    }
}
