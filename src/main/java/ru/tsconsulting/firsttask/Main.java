package ru.tsconsulting.firsttask;


import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class Main {

    private static Map<String, Department> mapOfDeparments = new HashMap<>();

    private static int maxLengthOfDepName = 0;
    private static int maxLengthofNameEmployee = 0;
    private static int maxLengthofSalary = 0;


    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Input data is incorrect");
            System.exit(1);
        }
        readingFile(args[0]);


        try (FileOutputStream f = new FileOutputStream((args[1]))) {
            System.setOut(new PrintStream(f,true,"UTF-8"));
            printAllDep(mapOfDeparments);
            dep2DepAllWays(mapOfDeparments);
        } catch (IOException e) {
            System.out.println("We have some problems with output file");
        }

    }

    public static void readingFile(String fileName) {

        try (BufferedReader reder = new BufferedReader(new InputStreamReader
                (new FileInputStream(fileName), "UTF-8"))) {
            String line;
            while ((line = reder.readLine()) != null) {
                String[] dataForOneLine = line.split(",");
                BigDecimal salary = new BigDecimal(dataForOneLine[2]).setScale(2, RoundingMode.CEILING);
                if (dataForOneLine.length != 3 || dataForOneLine[0] == null || dataForOneLine[1] == null ||
                        dataForOneLine[2] == null || dataForOneLine[0] == "" || dataForOneLine[1] == ""
                        || salary.signum() == -1) {
                    System.out.println("You have incorrect data input. Please change it");
                    break;
                }

                String nameOfDep = dataForOneLine[0];
                calculateMaxLengthsForGoodFormatPrint(nameOfDep, dataForOneLine[1], dataForOneLine[2]);
                Employee employee = new Employee(dataForOneLine[1], salary);
                if (!mapOfDeparments.containsKey(nameOfDep)) {
                    mapOfDeparments.put(nameOfDep, new Department(nameOfDep));
                }
                mapOfDeparments.get(nameOfDep).getListOfEmployees().add(employee);
            }
        } catch (IOException e) {
            System.out.println("I'm Sorry, i didn't find a file");
        }
    }

    public static void calculateMaxLengthsForGoodFormatPrint(String nameOfDep, String nameOfEmployee,
                                                             String salary) {
        if (nameOfDep.length() > maxLengthOfDepName) {
            maxLengthOfDepName = nameOfDep.length();
        }
        if (nameOfEmployee.length() > maxLengthofNameEmployee) {
            maxLengthofNameEmployee = nameOfEmployee.length();
        }
        if (salary.length() > maxLengthofSalary) {
            maxLengthofSalary = salary.length();
        }
    }

    public static void printAllDep(Map<String, Department> mapOfDeparments) {

        for (Map.Entry<String, Department> entry : mapOfDeparments.entrySet()) {
            String formatForDepName = "%-" + (maxLengthOfDepName + 25) + "s";
            String formatForEmpName = "%-" + (maxLengthofNameEmployee + 35) + "s";
            String formatForSalary = "%" + (maxLengthofSalary + 5) + "s\n";
            String formatAvg = "%-" + (35 + maxLengthofNameEmployee) + "s";

            System.out.printf(formatForDepName + "\n", entry.getKey());
            System.out.print("\n");


            Department department = entry.getValue();
            List<Employee> employeesList = department.getListOfEmployees();

            for (Employee employee : employeesList) {


                System.out.printf(formatForEmpName, employee.getName() + " ");
                System.out.printf(formatForSalary, employee.getSalary());
                System.out.print("\n");

            }
            System.out.printf(formatAvg, (entry.getKey() + " dep avg salary is"));
            System.out.printf(formatForSalary, department.getAvgSalary());
            System.out.print("\n");
            System.out.print("\n");

        }

    }

    public static void waysToTrasferFromDep2Dep(Department depFrom, Department depTo) {
        Set<ListEmployees> allWaysPrev = new HashSet<>();
        for (Employee employee : depFrom.getListOfEmployees()) {
            ListEmployees employeeList = new ListEmployees();
            employeeList.getListOfEmployees().add(employee);
            allWaysPrev.add(employeeList);
        }

        Set<ListEmployees> allWaysnext = new HashSet<>(allWaysPrev);

        for (int k = 1; k <= depFrom.getListOfEmployees().size(); k++) {

            if (k == 1) {
                Set<ListEmployees> allWaysPrevForOneEmp = new HashSet<>();
                for (ListEmployees listForOneEmp : allWaysPrev) {
                    if (isItPossibleToTransfer(depFrom, depTo, listForOneEmp)) {
                        allWaysPrevForOneEmp.add(listForOneEmp);
                    }
                }
                printTransf(depFrom, depTo, allWaysPrevForOneEmp);
                continue;
            }


            for (ListEmployees oneOfWays : allWaysPrev) {
                for (Employee employee : depFrom.getListOfEmployees()) {
                    if (!oneOfWays.getListOfEmployees().contains(employee)) {
                        List<Employee> listAddOneEmployee = new ArrayList<>(oneOfWays.getListOfEmployees());

                        listAddOneEmployee.add(employee);

                        ListEmployees listEmployeesExtraOne = new ListEmployees(listAddOneEmployee);

                        if (!isItTheSame(allWaysnext, listAddOneEmployee) &&
                                isItPossibleToTransfer(depFrom, depTo, listEmployeesExtraOne)) {
                            allWaysnext.add(listEmployeesExtraOne);
                        }
                    }

                }
            }

            allWaysnext.removeAll(allWaysPrev);
            allWaysPrev.clear();
            allWaysPrev.addAll(allWaysnext);
            printTransf(depFrom, depTo, allWaysnext);
        }
    }

    public static boolean isItTheSame(Set<ListEmployees> allWays, List<Employee> listToCheck) {
        for (ListEmployees listOfEmployee : allWays) {
            if ((listOfEmployee.getListOfEmployees().size() == listToCheck.size()) &&
                    listOfEmployee.getListOfEmployees().containsAll(listToCheck)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isItPossibleToTransfer(Department depFrom, Department depTo,
                                                 ListEmployees listEmployees) {
        List<Employee> employeesListForTransf = listEmployees.getListOfEmployees();
        if (depFrom.getListOfEmployees().size() == employeesListForTransf.size()) {
            return false;
        }

        BigDecimal totalSumNewDepFrom = depFrom.getTotalSalary().add(BigDecimal.ZERO);
        BigDecimal totalSumNewDepTo = depTo.getTotalSalary().add(BigDecimal.ZERO);
        for (Employee employee : employeesListForTransf) {
            totalSumNewDepFrom = totalSumNewDepFrom.subtract(employee.getSalary());
            totalSumNewDepTo = totalSumNewDepTo.add(employee.getSalary());
        }
        int countEmpFrom = depFrom.getListOfEmployees().size() -
                employeesListForTransf.size();
        int countEmpTo = depTo.getListOfEmployees().size() +
                (employeesListForTransf.size());

        BigDecimal avgSalaryNewFrom = totalSumNewDepFrom.divide(new BigDecimal(countEmpFrom),
                2, RoundingMode.HALF_UP);
        BigDecimal avgSalaryNewTo = totalSumNewDepTo.divide(new BigDecimal(countEmpTo),
                2, RoundingMode.HALF_UP);
        listEmployees.setDepFromNewAvgSalary(avgSalaryNewFrom);
        listEmployees.setDepToNewAvgSalary(avgSalaryNewTo);

        if (((depFrom.getAvgSalary().compareTo(avgSalaryNewFrom)) == -1) &&
                (depTo.getAvgSalary().compareTo(avgSalaryNewTo) == -1)) {
            return true;
        }
        return false;
    }

    public static void dep2DepAllWays(Map<String, Department> mapOfDeparments) {

        for (Map.Entry<String, Department> entry : mapOfDeparments.entrySet()) {
            String name = entry.getKey();
            Department department = entry.getValue();
            for (Map.Entry<String, Department> entryForComparing : mapOfDeparments.entrySet()) {
                String nameForComparing = entryForComparing.getKey();
                Department departmentForComparing = entryForComparing.getValue();
                if (!name.equals(nameForComparing)) {
                    waysToTrasferFromDep2Dep(department, departmentForComparing);
                    System.out.println();
                }
            }
        }
    }

    public static void printTransf(Department depFrom, Department depWhere,
                                   Set<ListEmployees> empTransf) {


        for (ListEmployees listOfEmpTransf : empTransf) {
            System.out.print("\n");

            for (Employee employee : listOfEmpTransf.getListOfEmployees()) {
                System.out.printf(employee.getName() + " ");
            }
            System.out.print("can transfer from " + depFrom.getNameOfDep() + " to " +
                    depWhere.getNameOfDep() + "\n");
            System.out.print("\n");

            System.out.printf("%-50s", "Avg Salary Before Transfer at " + depFrom.getNameOfDep() + " - ");
            System.out.printf("%50s\n", depFrom.getAvgSalary());
            System.out.print("\n");


            System.out.printf("%-50s", "Avg Salary After Transfer at " + depFrom.getNameOfDep() + " - ");
            System.out.printf("%50s\n", listOfEmpTransf.getDepFromNewAvgSalary());
            System.out.print("\n");


            System.out.printf("%-50s", "Avg Salary Before Transfer at " + depWhere.getNameOfDep() + " - ");
            System.out.printf("%50s\n", depWhere.getAvgSalary());
            System.out.print("\n");


            System.out.printf("%-50s", "Avg Salary After Transfer at " + depWhere.getNameOfDep() + " - ");
            System.out.printf("%50s\n\n", listOfEmpTransf.getDepToNewAvgSalary());
            System.out.print("\n");


        }
    }

}

