package com.esl.internship.staffsync.commons.util;

import com.esl.internship.staffsync.commons.model.Employee;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

public class Utility {

    public static String stringifyEmployee(Employee employee) {
        return stringifyEmployeeMaker(employee)
                .toString();
    }

    public static String stringifyEmployee(Employee employee, String remarks) {
        return stringifyEmployeeMaker(employee)
                .put("remarks", remarks)
                .toString();
    }

    private static ObjectNode stringifyEmployeeMaker(Employee employee) {
        return Json.newObject()
                .put("employeeId", employee.getEmployeeId())
                .put("name", employee.getFullName())
                .put("email", employee.getEmployeeEmail());
    }

}
