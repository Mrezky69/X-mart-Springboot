package com.project.spring;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.spring.dto.BarangRequest;
import com.project.spring.dto.BarangResponse;
import com.project.spring.service.BarangService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
class BarangControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BarangService barangService;

    @Test
    void testListBarang() throws Exception {
        BarangResponse barang = new BarangResponse();
        barang.setId(UUID.randomUUID());
        barang.setNamaBarang("Test Barang");
        barang.setHargaSatuan(1000.0);
        Mockito.when(barangService.listBarang()).thenReturn(Collections.singletonList(barang));

        mockMvc.perform(get("/api/barang/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].namaBarang").value("Test Barang"))
                .andExpect(jsonPath("$[0].hargaSatuan").value(1000.0));
    }

    @Test
    void testGetBarangById_NotFound() throws Exception {
        Mockito.when(barangService.getBarangById(Mockito.anyString())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Data tidak ditemukan."));

        mockMvc.perform(get("/api/barang/invalid-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetBarangById_Success() throws Exception {
        BarangResponse barang = new BarangResponse();
        UUID id = UUID.randomUUID();
        barang.setId(id);
        barang.setNamaBarang("Test Barang");
        barang.setHargaSatuan(1000.0);
        Mockito.when(barangService.getBarangById(id.toString())).thenReturn(barang);

        mockMvc.perform(get("/api/barang/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.namaBarang").value("Test Barang"))
                .andExpect(jsonPath("$.hargaSatuan").value(1000.0));
    }


    @Test
    void testAddBarang_Success() throws Exception {
        BarangRequest request = new BarangRequest();
        request.setNama("Test Barang");
        request.setHarga(1000.0);

        BarangResponse response = new BarangResponse();
        response.setId(UUID.randomUUID());
        response.setNamaBarang("Test Barang");
        response.setHargaSatuan(1000.0);

        Mockito.when(barangService.add(Mockito.any(BarangRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/barang/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.namaBarang").value("Test Barang"))
                .andExpect(jsonPath("$.hargaSatuan").value(1000.0));
    }

    @Test
    void testUpdateBarang_Success() throws Exception {
        UUID id = UUID.randomUUID();
        BarangRequest request = new BarangRequest();
        request.setNama("Updated Barang");
        request.setHarga(1500.0);

        BarangResponse response = new BarangResponse();
        response.setId(id);
        response.setNamaBarang("Updated Barang");
        response.setHargaSatuan(1500.0);

        Mockito.when(barangService.updateBarang(Mockito.eq(id), Mockito.any(BarangRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/barang/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.namaBarang").value("Updated Barang"))
                .andExpect(jsonPath("$.hargaSatuan").value(1500.0));
    }

    @Test
    void testDeleteBarang_Success() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.doNothing().when(barangService).deleteBarang(id);

        mockMvc.perform(delete("/api/barang/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
