package ru.tsconsulting.firsttask;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Department {
    private List<Employee> listOfEmployees = new ArrayList<>();
    private String nameOfDep;

    public Department(String nameOfDep) {
        this.nameOfDep = nameOfDep;
    }

    public String getNameOfDep() {
        return nameOfDep;
    }

    public void setNameOfDep(String nameOfDep) {
        this.nameOfDep = nameOfDep;
    }

    public List<Employee> getListOfEmployees() {
        return listOfEmployees;
    }

    public BigDecimal getTotalSalary() {
        BigDecimal totalSalary = new BigDecimal(0);
        for (Employee employee : listOfEmployees) {
            totalSalary = totalSalary.add(employee.getSalary());
        }
        return totalSalary;
    }

    public BigDecimal getAvgSalary() {

        return getTotalSalary().divide(new BigDecimal(listOfEmployees.size()), 2, RoundingMode.CEILING);
    }

    public void setListOfEmployees(List listOfEmployees) {
        this.listOfEmployees = listOfEmployees;
    }

}
