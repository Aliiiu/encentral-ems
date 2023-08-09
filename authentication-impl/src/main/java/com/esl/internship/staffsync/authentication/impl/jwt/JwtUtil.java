package com.esl.internship.staffsync.authentication.impl.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.esl.internship.staffsync.authentication.model.EmployeeAuthInfo;
import com.typesafe.config.ConfigFactory;

import java.util.Date;

/**
 * @author DEMILADE
 * @dateCreated 09/08/2023
 * @description Class containing methods for operating on JWTs
 */
public class JwtUtil {

    //TODO: Changes to a more secure key
    private final String secret = ConfigFactory.load().getString("play.crypto.secret");

    private final Algorithm algorithm = Algorithm.HMAC256(secret);
    //15min
    private final long expirationMillis = 900000;


    /**
     * @author DEMILADE
     * @dateCrested 09/09/2023
     * @description Method to genertae a token
     *
     * @param employeeAuthInfo Class containing fields to be placed in JWT payload
     *
     * @return Stringified token
     */
    public String generateToken(EmployeeAuthInfo employeeAuthInfo) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationMillis);

        return JWT.create()
                .withClaim("employeeId", employeeAuthInfo.getEmployeeId())
                .withClaim("roleId", employeeAuthInfo.getRoleId())
                .withClaim("firstName", employeeAuthInfo.getFirstName())
                .withClaim("lastName", employeeAuthInfo.getLastName())
                .withClaim("employeeEmail", employeeAuthInfo.getEmployeeEmail())
                .withExpiresAt(expiration)
                .sign(algorithm);
    }

    public DecodedJWT verifyToken(String token) {
        DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
        return jwt;
    }
}
