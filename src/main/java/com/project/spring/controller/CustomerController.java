package com.project.spring.controller;

import com.project.spring.dto.*;
import com.project.spring.service.CustomerService;
import com.project.spring.util.ResponseHandler;

import jakarta.validation.Valid;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.project.spring.util.ResponseMessage.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

    @PostMapping ("/add")
    public ResponseEntity<Object> addCustomer(@Valid @RequestBody CustomerRequest customer) {
        try {
            CustomerResponse response = customerService.addCustomer(customer);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<CustomerResponse>> listCustomers() {
        List<CustomerResponse> customers = customerService.listCustomer();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable String id) {
        CustomerResponse customer = customerService.getCustomerById(id);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable UUID id,
            @Valid @RequestBody CustomerRequest customer) {
        CustomerResponse updatedCustomer = customerService.updateCustomer(id, customer);
        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable UUID id) {
        customerService.deleteCustomer(id);
        return new ResponseEntity<>(SUCCESS_DELETE, HttpStatus.OK);
    }

    @GetMapping("/transaksi")
    public ResponseEntity<List<TransaksiResponse>> getTransaksiByQrCode(@RequestParam String qrCode) {
        var response = customerService.detailCustomer(qrCode);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
