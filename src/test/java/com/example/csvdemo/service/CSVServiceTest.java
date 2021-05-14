package com.example.csvdemo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.csvdemo.model.CSVUploadResponse;
import com.example.csvdemo.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class CSVServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private CSVService         csvService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(csvService, "csvHeaders", new String[] { "Employee Id", "Employee Name", "Age", "Country" });
    }

    @Test
    void testUpload_should_throw_exception() throws IOException {
        assertThrows(RuntimeException.class, () -> csvService.upload(null));
    }

    @Test
    void testUpload_should_work() throws IOException {
        InputStream uploadStream = CSVService.class.getClassLoader().getResourceAsStream("test.csv");
        MockMultipartFile file = new MockMultipartFile("test", "test", CSVService.TYPE, uploadStream);
        when(employeeRepository.saveAll(Mockito.anyList())).thenReturn(null);
        CSVUploadResponse response = csvService.upload(file);
        assertNotNull(response);
    }
    
    @Test
    void testUpload_should_skip_error_records() throws IOException {
        InputStream uploadStream = CSVService.class.getClassLoader().getResourceAsStream("test2.csv");
        MockMultipartFile file = new MockMultipartFile("test", "test", CSVService.TYPE, uploadStream);
        when(employeeRepository.saveAll(Mockito.anyList())).thenReturn(null);
        CSVUploadResponse response = csvService.upload(file);
        assertNotNull(response);
    }

    @Test
    void testUpload_should_return_13_employees_with_2_failures() throws IOException {
        InputStream uploadStream = CSVService.class.getClassLoader().getResourceAsStream("test.csv");
        MockMultipartFile file = new MockMultipartFile("test", "test", CSVService.TYPE, uploadStream);

        Iterable<CSVRecord> csvRecords = csvService.getCSVRecords(file);

        var employeeAndFailCount = csvService.createEmployees(csvRecords);

        assertEquals(13, employeeAndFailCount.getFirst().size());
        assertEquals(2, employeeAndFailCount.getSecond());
    }

    @Test
    void testUpload_should_skip_count_error_records_and_return_15_employees() throws IOException {
        InputStream uploadStream = CSVService.class.getClassLoader().getResourceAsStream("test2.csv");
        MockMultipartFile file = new MockMultipartFile("test", "test", CSVService.TYPE, uploadStream);

        Iterable<CSVRecord> csvRecords = csvService.getCSVRecords(file);

        var employeeAndFailCount = csvService.createEmployees(csvRecords);

        assertEquals(15, employeeAndFailCount.getFirst().size());
        assertEquals(0, employeeAndFailCount.getSecond());
    }
}
