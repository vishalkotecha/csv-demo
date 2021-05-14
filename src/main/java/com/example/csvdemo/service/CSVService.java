package com.example.csvdemo.service;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.csvdemo.entity.Employee;
import com.example.csvdemo.model.CSVUploadResponse;
import com.example.csvdemo.repository.EmployeeRepository;

@Service
public class CSVService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CSVService.class);
    public static String        TYPE   = "text/csv";

    @Value("${employees.csv.headers}")
    private String[]            csvHeaders;

    @Autowired
    private EmployeeRepository  repository;

    @Transactional
    public CSVUploadResponse upload(MultipartFile file) throws IOException {
        validateCSV(file);
        var employeesAndFailCount = createEmployees(getCSVRecords(file));

        repository.saveAll(employeesAndFailCount.getFirst());

        return new CSVUploadResponse(employeesAndFailCount.getFirst().size(), employeesAndFailCount.getSecond());
    }

    Iterable<CSVRecord> getCSVRecords(MultipartFile file) throws IOException {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            return csvParser.getRecords();
        }
    }

    Pair<List<Employee>, Integer> createEmployees(Iterable<CSVRecord> csvRecords) {
        var parsedEmployees =
          StreamSupport.stream(csvRecords.spliterator(), false)
            .map(this::createEmployee)
            .collect(partitioningBy(Optional::isPresent, mapping(employeeOptional -> employeeOptional.orElse(null), toList())));

        return Pair.of(parsedEmployees.get(true), parsedEmployees.get(false).size());
    }

    Optional<Employee> createEmployee(CSVRecord csvRecord) {
        try {
            return Optional.of(new Employee(Long.parseLong(csvRecord.get(csvHeaders[0])), csvRecord.get(csvHeaders[1]), Integer.parseInt(csvRecord.get(csvHeaders[2])),
              csvRecord.get(csvHeaders[2])));
        } catch (Exception e) {
            LOGGER.error("Couldn't parse record {}", e.getMessage());
            return Optional.empty();
        }
    }

    private static void validateCSV(MultipartFile file) {
        if(file == null) throw new RuntimeException("Invalid input!");
        if (!TYPE.equals(file.getContentType())) {
            throw new RuntimeException("Please upload a valid csv file!");
        }
    }

    public List<Employee> getAll() {
        return repository.findAll();
    }
}
