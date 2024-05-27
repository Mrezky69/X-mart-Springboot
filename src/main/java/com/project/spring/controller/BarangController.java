package com.project.spring.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.project.spring.dto.*;
import com.project.spring.service.BarangService;
import com.project.spring.util.ResponseHandler;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/barang")
public class BarangController {
    
    @Autowired
    private BarangService barangService;

    @GetMapping("/list")
    public ResponseEntity<List<BarangResponse>> listBarang() {
        List<BarangResponse> barang = barangService.listBarang();
        return new ResponseEntity<>(barang, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarangResponse> getBarangById(@PathVariable String id) {
        BarangResponse Barang = barangService.getBarangById(id);
        return new ResponseEntity<>(Barang, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BarangResponse> updateBarang(@PathVariable UUID id,
            @Valid @RequestBody BarangRequest Barang) {
        BarangResponse updatedBarang = barangService.updateBarang(id, Barang);
        return new ResponseEntity<>(updatedBarang, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBarang(@PathVariable UUID id) {
        barangService.deleteBarang(id);
        return new ResponseEntity<>("Barang deleted successfully", HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addBarang(@Valid @RequestBody BarangRequest barang) {
        try {
            BarangResponse response = barangService.add(barang);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
