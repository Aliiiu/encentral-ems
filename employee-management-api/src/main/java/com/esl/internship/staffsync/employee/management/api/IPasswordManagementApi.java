package com.esl.internship.staffsync.employee.management.api;

public interface IPasswordManagementApi {

    String hashPassword(String password);

    boolean verifyPassword(String passwordHash, String password);

}
