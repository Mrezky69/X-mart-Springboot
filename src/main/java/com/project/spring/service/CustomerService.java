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
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final TransaksiRepository transaksiRepository;
    private final BarangRepository barangRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, TransaksiRepository transaksiRepository, BarangRepository barangRepository) {
        this.customerRepository = customerRepository;
        this.transaksiRepository = transaksiRepository;
        this.barangRepository = barangRepository;
    }

    public CustomerResponse addCustomer(CustomerRequest customer) {
            Customer existingCustomer = customerRepository.findByNama(customer.getNama());
            if (existingCustomer != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, DATA_ALREADY);
            }
            Customer newCustomer = new Customer();
            newCustomer.setNama(customer.getNama());
            newCustomer.setQrCode(UUID.randomUUID().toString());
            newCustomer.setWallet(customer.getWallet());
            customerRepository.save(newCustomer);
            return CustomerResponse.builder()
                    .id(newCustomer.getId())
                    .nama(newCustomer.getNama())
                    .qrCode(newCustomer.getQrCode())
                    .wallet(newCustomer.getWallet())
                    .build();
    }


    public List<CustomerResponse> listCustomer() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerResponse> responses = new ArrayList<>();
        for (Customer data : customers) {
            responses.add(CustomerResponse.builder().id(data.getId()).nama(data.getNama()).qrCode(data.getQrCode())
                    .wallet(data.getWallet()).build());
        }
        return responses;
    }
    
    public List<TransaksiResponse> detailCustomer(String qrCode) {
        List<Transaksi> transaksiList = transaksiRepository.findByQrCode(qrCode);
        Customer existingCustomer = customerRepository.findByQrCode(qrCode);
        List<TransaksiResponse> response = new ArrayList<>();
        for (Transaksi transaksi : transaksiList) {
            Barang barangs = barangRepository.findByRfid(transaksi.getRfid());
            response.add(TransaksiResponse.builder()
                    .id(transaksi.getId())
                    .qrCode(transaksi.getQrCode())
                    .rfid(barangs.getNamaBarang())
                    .hargaSatuan(transaksi.getHargaSatuan())
                    .jumlah(transaksi.getJumlah())
                    .tanggalJam(transaksi.getTanggalJam())
                    .wallet(existingCustomer.getWallet())
                    .nama(existingCustomer.getNama())
                    .build());
        }
        return response;
    }

    public CustomerResponse getCustomerById(String id) {
        Customer existingCustomer = customerRepository.findByQrCode(id);
        if (existingCustomer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND);
        }
        return CustomerResponse.builder()
                .id(existingCustomer.getId())
                .nama(existingCustomer.getNama())
                .qrCode(existingCustomer.getQrCode())
                .wallet(existingCustomer.getWallet())
                .build();
    }

    public CustomerResponse updateCustomer(UUID id, CustomerRequest customer) {
            Optional<Customer> existingCustomerOpt = customerRepository.findById(id);
            if (existingCustomerOpt.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND);
            }
            
            Customer existingCustomer = existingCustomerOpt.get();
            existingCustomer.setNama(customer.getNama());
            existingCustomer.setQrCode(existingCustomer.getQrCode());
            existingCustomer.setWallet(customer.getWallet());

            Customer updatedCustomer = customerRepository.save(existingCustomer);

            return CustomerResponse.builder()
                    .id(updatedCustomer.getId())
                    .nama(updatedCustomer.getNama())
                    .qrCode(updatedCustomer.getQrCode())
                    .wallet(updatedCustomer.getWallet())
                    .build();
    }

    public void deleteCustomer(UUID id) {
            Optional<Customer> existingCustomer = customerRepository.findById(id);
            if (existingCustomer.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND);
            }
            customerRepository.delete(existingCustomer.get());
    }
}
