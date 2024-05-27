package com.project.spring.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.spring.model.Barang;

@Repository
public interface BarangRepository extends JpaRepository<Barang, UUID> {
    Barang findByNamaBarang(String nama);
    Barang findByRfid(String rfid);
}
