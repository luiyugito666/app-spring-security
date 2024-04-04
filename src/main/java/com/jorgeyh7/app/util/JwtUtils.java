package com.jorgeyh7.app.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    //Project0Appv1.0
    @Value("${security.jwt.keyprivateproject}")
    private String privateKey;
    @Value("${security.jwt.user-generator}")
    private String usergenerator;


    //create token
    public String createToken(Authentication authentication){
        Algorithm algorithm= Algorithm.HMAC256(this.privateKey);
        String username = authentication.getPrincipal().toString();
        //make list string split with ","

        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));//READ,CREATE,DELETE


        return JWT.create()
        .withIssuer(this.usergenerator)
        .withSubject(username)
        .withClaim("authorities",authorities)
        .withIssuedAt(new Date())
        .withExpiresAt(new Date( System.currentTimeMillis()+1800000))
        .withJWTId(UUID.randomUUID().toString())
        .withNotBefore(new Date(System.currentTimeMillis()))
        .sign(algorithm);
    }

    public DecodedJWT validateToken(String token){
        try {
            Algorithm algorithm= Algorithm.HMAC256(this.privateKey);
            JWTVerifier verifier=JWT.require(algorithm)
                    .withIssuer(usergenerator)
                    .build();

          return verifier.verify(token);

        }catch (JWTVerificationException jwtVerificationException){
            throw new JWTVerificationException("token is invalid, not authorized");
        }


    }

    public String extractUsername(DecodedJWT decodedJWT){
        return decodedJWT.getSubject().toString();
    }

    public Claim getSpecificClaim(DecodedJWT decodedJWT,String claimName){

return decodedJWT.getClaim(claimName);
    }

    public Map<String, Claim> getAllClaims(DecodedJWT decodedJWT){
        return decodedJWT.getClaims();
    }


}
