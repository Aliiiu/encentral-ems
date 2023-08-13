package com.esl.internship.staffsync.employee.management.impl;

import com.esl.internship.staffsync.employee.management.api.IPasswordManagementApi;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author WARITH
 * @dateCreated 09/08/2023
 * @description A password management implementation using SHA-236
 */
public class DefaultPasswordManagementApiImpl implements IPasswordManagementApi {

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Hash password
     *
     * @param password Password to hash
     *
     * @return String PasswordHash
     */
    @Override
    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array to hexadecimal format
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @author WARITH
     * @dateCreated 09/08/2023
     * @description Check a plain password against a hash value
     *
     * @param passwordHash Hash to check against
     * @param password Password to check
     *
     * @return boolean True is password matches, false otherwise
     */
    @Override
    public boolean verifyPassword(String passwordHash, String password) {
        return hashPassword(password).equals(passwordHash);
    }

}
