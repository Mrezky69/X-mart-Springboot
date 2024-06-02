package com.project.spring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.project.spring.dto.BarangRequest;
import com.project.spring.dto.BarangResponse;
import com.project.spring.model.Barang;
import com.project.spring.repository.BarangRepository;
import com.project.spring.service.BarangService;

@ExtendWith(MockitoExtension.class)
class BarangServiceTest {

    @Mock
    private BarangRepository barangRepository;

    @InjectMocks
    private BarangService barangService;

    @Test
    void testListBarang() {
        List<Barang> barangList = Arrays.asList(
                new Barang(UUID.randomUUID(), "123456", "Barang A", 1000.0),
                new Barang(UUID.randomUUID(), "789012", "Barang B", 2000.0));
        when(barangRepository.findAll()).thenReturn(barangList);

        List<BarangResponse> response = barangService.listBarang();

        assertEquals(2, response.size());
        assertEquals("Barang A", response.get(0).getNamaBarang());
        assertEquals("Barang B", response.get(1).getNamaBarang());
    }

    @Test
    void testAddBarang() {
        BarangRequest request = new BarangRequest();
        request.setNama("Barang A");
        request.setHarga(1000.0);

        Barang savedBarang = new Barang(UUID.randomUUID(), "123456", "Barang A", 1000.0);
        when(barangRepository.findByNamaBarang("Barang A")).thenReturn(null);
        when(barangRepository.save(any(Barang.class))).thenReturn(savedBarang);

        BarangResponse response = barangService.add(request);

        assertEquals("Barang A", response.getNamaBarang());
        assertEquals(1000.0, response.getHargaSatuan());
    }

    @Test
    void testUpdateBarang() {
        UUID id = UUID.randomUUID();
        Barang existingBarang = new Barang(id, "123456", "Barang A", 1000.0);

        BarangRequest request = new BarangRequest();
        request.setNama("Barang B");
        request.setHarga(1500.0);

        when(barangRepository.findById(id)).thenReturn(Optional.of(existingBarang));
        when(barangRepository.save(any(Barang.class))).thenReturn(existingBarang);

        BarangResponse response = barangService.updateBarang(id, request);

        assertEquals("Barang B", response.getNamaBarang());
        assertEquals(1500.0, response.getHargaSatuan());
    }

    @Test
    void testDeleteBarang() {
        UUID id = UUID.randomUUID();
        Barang existingBarang = new Barang(id, "123456", "Barang A", 1000.0);

        when(barangRepository.findById(id)).thenReturn(Optional.of(existingBarang));
        doNothing().when(barangRepository).delete(existingBarang);

        assertDoesNotThrow(() -> barangService.deleteBarang(id));
        verify(barangRepository, times(1)).delete(existingBarang);
    }
}
