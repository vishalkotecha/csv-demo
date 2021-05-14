package com.example.csvdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.csvdemo.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
