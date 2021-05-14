package com.example.csvdemo.model;

import java.util.ArrayList;
import java.util.List;

import com.example.csvdemo.entity.Employee;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class CSVUploadResponse {

    private int noOfProcessedLines;
    private int noOfSkippedLines;
    @JsonIgnore
    private List<Employee> employees = new ArrayList<>();

    @SuppressWarnings("unused")
    private CSVUploadResponse() {
    }

    public CSVUploadResponse(int noOfProcessedLines, int noOfSkippedLines) {
        super();
        this.noOfProcessedLines = noOfProcessedLines;
        this.noOfSkippedLines = noOfSkippedLines;
    }
    
    public CSVUploadResponse(int noOfProcessedLines, int noOfSkippedLines, List<Employee> employees) {
        super();
        this.noOfProcessedLines = noOfProcessedLines;
        this.noOfSkippedLines = noOfSkippedLines;
        this.employees = employees;
    }

    
    
    public List<Employee> getEmployees() {
        return employees;
    }

    public int getNoOfProcessedLines() {
        return noOfProcessedLines;
    }

    public int getNoOfSkippedLines() {
        return noOfSkippedLines;
    }
    
    

}
