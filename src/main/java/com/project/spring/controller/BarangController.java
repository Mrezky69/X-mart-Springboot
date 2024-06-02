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
import static com.project.spring.util.ResponseMessage.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/barang")
public class BarangController {

    private final BarangService barangService;

    @Autowired
    public BarangController(BarangService barangService) {
        this.barangService = barangService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<BarangResponse>> listBarang() {
        List<BarangResponse> barang = barangService.listBarang();
        return new ResponseEntity<>(barang, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarangResponse> getBarangById(@PathVariable String id) {
        BarangResponse barang = barangService.getBarangById(id);
        return new ResponseEntity<>(barang, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BarangResponse> updateBarang(@PathVariable UUID id,
            @Valid @RequestBody BarangRequest barang) {
        BarangResponse updatedBarang = barangService.updateBarang(id, barang);
        return new ResponseEntity<>(updatedBarang, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBarang(@PathVariable UUID id) {
        barangService.deleteBarang(id);
        return new ResponseEntity<>(SUCCESS_DELETE, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addBarang(@Valid @RequestBody BarangRequest barang) {
        try {
            BarangResponse response = barangService.add(barang);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
