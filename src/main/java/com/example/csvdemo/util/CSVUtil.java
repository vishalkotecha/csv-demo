package com.example.csvdemo.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.example.csvdemo.entity.Employee;
import com.example.csvdemo.service.CSVService;

public class CSVUtil {
    private static final Logger LOGGER  = LoggerFactory.getLogger(CSVService.class);
    public static String        TYPE    = "text/csv";
    static String[]             HEADERs = { "Employee Id", "Employee Name", "Age", "Country" };

    public static List<Employee> load(InputStream is) {
        int failCount = 0;
        int processedCount = 0;
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            List<Employee> employees = new ArrayList<>();
            for (CSVRecord csvRecord : csvRecords) {
                try {
                    Employee employee = new Employee(Long.parseLong(csvRecord.get("Employee Id")), csvRecord.get("Employee Name"), Integer.parseInt(csvRecord.get("Age")), csvRecord.get("Country"));
                    employees.add(employee);
                    processedCount++;
                } catch (Exception e) {
                    failCount++;
                    LOGGER.error("Error while parsing a record {}", e.getMessage(), e);
                }
            }
            LOGGER.debug("Fail count = {} ", failCount);
            LOGGER.debug("Processed count = {} ", processedCount);
            return employees;
        }catch (Exception e) {
            throw new RuntimeException("failed to parse CSV file: " + e.getMessage());
        }
    }

    public static void validateCSV(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            String message = "Please upload a csv file!";
            throw new RuntimeException(message);
        }
    }

}
