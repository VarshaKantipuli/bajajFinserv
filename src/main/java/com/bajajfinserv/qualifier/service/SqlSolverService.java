package com.bajajfinserv.qualifier.service;

import org.springframework.stereotype.Service;

@Service
public class SqlSolverService {

    public String getSqlSolution(String regNo) {
        // Extract last two digits
        String lastTwoDigits = regNo.substring(regNo.length() - 2);
        int lastDigit = Integer.parseInt(lastTwoDigits) % 10;
        
        // Determine if odd or even
        if (lastDigit % 2 == 0) {
            return getEvenSolution();
        } else {
            return getOddSolution();
        }
    }

    private String getOddSolution() {
        // Question 1 - For ODD registration numbers
        String sqlQuery = "WITH FilteredPayments AS ( " +
            "SELECT e.EMP_ID, e.FIRST_NAME, e.LAST_NAME, e.DOB, e.DEPARTMENT, p.AMOUNT, p.PAYMENT_TIME " +
            "FROM EMPLOYEE e JOIN PAYMENTS p ON e.EMP_ID = p.EMP_ID " +
            "WHERE DAY(p.PAYMENT_TIME) != 1 ), " +
            "EmployeeTotalSalary AS ( " +
            "SELECT EMP_ID, FIRST_NAME, LAST_NAME, DOB, DEPARTMENT, SUM(AMOUNT) AS TOTAL_SALARY " +
            "FROM FilteredPayments " +
            "GROUP BY EMP_ID, FIRST_NAME, LAST_NAME, DOB, DEPARTMENT ), " +
            "RankedEmployees AS ( " +
            "SELECT d.DEPARTMENT_NAME, ets.TOTAL_SALARY AS SALARY, " +
            "CONCAT(ets.FIRST_NAME, ' ', ets.LAST_NAME) AS EMPLOYEE_NAME, " +
            "TIMESTAMPDIFF(YEAR, ets.DOB, CURDATE()) AS AGE, " +
            "ROW_NUMBER() OVER (PARTITION BY d.DEPARTMENT_ID ORDER BY ets.TOTAL_SALARY DESC) AS rn " +
            "FROM EmployeeTotalSalary ets JOIN DEPARTMENT d ON ets.DEPARTMENT = d.DEPARTMENT_ID ) " +
            "SELECT DEPARTMENT_NAME, SALARY, EMPLOYEE_NAME, AGE " +
            "FROM RankedEmployees WHERE rn = 1 ORDER BY DEPARTMENT_NAME";
        return sqlQuery;
    }

    private String getEvenSolution() {
        // Question 2 - For EVEN registration numbers
        String sqlQuery = "WITH EmployeeAges AS ( " +
            "SELECT e.EMP_ID, e.FIRST_NAME, e.LAST_NAME, e.DEPARTMENT, " +
            "TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE, p.AMOUNT " +
            "FROM EMPLOYEE e JOIN PAYMENTS p ON e.EMP_ID = p.EMP_ID " +
            "WHERE p.AMOUNT > 70000 ), " +
            "DepartmentStats AS ( " +
            "SELECT d.DEPARTMENT_ID, d.DEPARTMENT_NAME, AVG(ea.AGE) AS AVERAGE_AGE, " +
            "GROUP_CONCAT(CONCAT(ea.FIRST_NAME, ' ', ea.LAST_NAME) ORDER BY ea.EMP_ID SEPARATOR ', ') AS EMPLOYEE_LIST " +
            "FROM DEPARTMENT d JOIN EmployeeAges ea ON d.DEPARTMENT_ID = ea.DEPARTMENT " +
            "GROUP BY d.DEPARTMENT_ID, d.DEPARTMENT_NAME ) " +
            "SELECT DEPARTMENT_NAME, ROUND(AVERAGE_AGE, 2) AS AVERAGE_AGE, " +
            "SUBSTRING_INDEX(EMPLOYEE_LIST, ', ', 10) AS EMPLOYEE_LIST " +
            "FROM DepartmentStats ORDER BY DEPARTMENT_ID DESC";
        return sqlQuery;
    }
}