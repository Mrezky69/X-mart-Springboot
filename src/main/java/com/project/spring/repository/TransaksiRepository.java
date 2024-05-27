package com.project.spring.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.project.spring.model.Transaksi;

@Repository
public interface TransaksiRepository extends JpaRepository <Transaksi, Long> {
    List<Transaksi> findByQrCode(String qrCode);    
}
