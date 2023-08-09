package com.esl.internship.staffsync.authentication.impl.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.esl.internship.staffsync.authentication.model.EmployeeAuthInfo;


/**
 * @author DEMILADE
 * @dateCreated 09/08/2023
 * @description Class containing methods for operating on JWTs
 */
public class JwtUtil {

    //TODO: Changes to a more secure key
    private static final String SECRET_KEY = "secret-key";

    /**
     * @author DEMILADE
     * @dateCrested 09/09/2023
     * @description Method to genertae a token
     *
     * @param employeeAuthInfo Class containing fields to be placed in JWT payload
     *
     * @return Stringified token
     */
    public static String generateToken(EmployeeAuthInfo employeeAuthInfo) {
        return JWT.create()
                .withClaim("employeeId", employeeAuthInfo.getEmployeeId())
                .withClaim("roleId", employeeAuthInfo.getRoleId())
                .withClaim("firstName", employeeAuthInfo.getFirstName())
                .withClaim("lastName", employeeAuthInfo.getLastName())
                .withClaim("employeeEmail", employeeAuthInfo.getEmployeeEmail())
                .withClaim("employeeActive", employeeAuthInfo.getEmployeeActive())
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }
}
