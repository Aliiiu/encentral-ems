package com.esl.internship.staffsync.employee.management.impl;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.esl.internship.staffsync.employee.management.api.IPasswordManagementApi;


/**
 * @author WARITH
 * @dateCreated 09/08/2023
 * @description A password management implementation using BCrypt
 */
public class PasswordManagementApiBcryptImpl implements IPasswordManagementApi {
    @Override
    public String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    @Override
    public boolean verifyPassword(String passwordHash, String password) {
        return BCrypt.verifyer().verify(password.toCharArray(), passwordHash).verified;
    }
}
