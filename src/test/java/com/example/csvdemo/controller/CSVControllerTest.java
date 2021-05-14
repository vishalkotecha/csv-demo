package com.example.csvdemo.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.csvdemo.model.CSVUploadResponse;
import com.example.csvdemo.service.CSVService;

@WebMvcTest(CSVController.class)
class CSVControllerTest {

    @Autowired
    private MockMvc    mockMvc;

    @MockBean
    private CSVService csvService;

    @Test
    public void testUploadFile() throws Exception {
        InputStream uploadStream = CSVControllerTest.class.getClassLoader().getResourceAsStream("test.csv");
        MockMultipartFile file = new MockMultipartFile("file", uploadStream);
        assert uploadStream != null;

        when(csvService.upload(file)).thenReturn(new CSVUploadResponse(0, 0));

        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/upload").file(file)).andExpect(status().isOk());
    }

}
