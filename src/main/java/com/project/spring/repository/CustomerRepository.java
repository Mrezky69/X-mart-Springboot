package com.project.spring.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.spring.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository <Customer, UUID> {
    Customer findByNama(String nama);
    Customer findByQrCode(String qrCode);
}
