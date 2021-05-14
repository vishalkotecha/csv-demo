package com.example.csvdemo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "employee")
public class Employee implements Serializable{
    private static final long serialVersionUID = -5271524616820860966L;

    @Id
    @Column(name = "id")
    private long   id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private int    age;

    @Column(name = "country")
    private String country;

    @SuppressWarnings("unused")
    private Employee() {
    }

    public Employee(long id, String name, int age, String country) {
        super();
        this.id = id;
        this.name = name;
        this.age = age;
        this.country = country;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}
