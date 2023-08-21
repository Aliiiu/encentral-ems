package com.esl.internship.staffsync.entities.stringify;

import com.esl.internship.staffsync.entities.JpaEmployee;
import com.esl.internship.staffsync.entities.JpaDepartment;
import play.libs.Json;

/**
 * @author WARITH
 * @dateCreated 17/08/2023
 * @description This class is strictly for converting Jpa entities to json string using only few attributes
 * Useful for when returning a model of a Jpa Entity that has a foreign related table.
 */
public class Stringifier {

    public static String stringifyJpaEmployee(JpaEmployee jpaEmployee) {
        return Json.newObject()
                .put("employeeId", jpaEmployee.getEmployeeId())
                .put("firstName", jpaEmployee.getFirstName())
                .put("lastName", jpaEmployee.getLastName())
                .toString();
    }
}


