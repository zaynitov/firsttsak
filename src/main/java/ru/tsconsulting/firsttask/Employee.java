package ru.tsconsulting.firsttask;


import java.math.BigDecimal;

public class Employee {
    private BigDecimal salary;
    private String name;

    public Employee(String name, BigDecimal salary) {
        this.salary = salary;
        this.name = name;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public void setName(String name) {
        this.name = name;
    }


    public BigDecimal getSalary() {
        return salary;
    }

    public String getName() {
        return name;
    }
}
