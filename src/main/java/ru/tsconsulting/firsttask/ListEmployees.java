package ru.tsconsulting.firsttask;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ListEmployees {
    private List<Employee> listOfEmployees = new ArrayList<>();
    private BigDecimal depFromNewAvgSalary;
    private BigDecimal depToNewAvgSalary;

    public ListEmployees(List<Employee> listOfEmployees) {
        this.listOfEmployees = listOfEmployees;
    }

    public ListEmployees() {
    }

    public List<Employee> getListOfEmployees() {
        return listOfEmployees;
    }

    public BigDecimal getDepFromNewAvgSalary() {

        return depFromNewAvgSalary;
    }

    public BigDecimal getDepToNewAvgSalary() {
        return depToNewAvgSalary;
    }

    public void setListOfEmployees(List<Employee> listOfEmployees) {
        this.listOfEmployees = listOfEmployees;
    }

    public void setDepFromNewAvgSalary(BigDecimal depFromNewAvgSalary) {
        this.depFromNewAvgSalary = depFromNewAvgSalary;
    }

    public void setDepToNewAvgSalary(BigDecimal depToNewAvgSalary) {
        this.depToNewAvgSalary = depToNewAvgSalary;
    }


}