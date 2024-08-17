package com.niit.UserAuthenticationService.security;

import com.niit.UserAuthenticationService.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Service
public class JWTSecurityTokenGenerator implements SecurityTokenGenerator{
    @Override
    public String createToken(User user) {
        Map<String,Object> claims=new HashMap<>();
        claims.put("userId",user.getUserId());
        System.out.println(claims);
        return generateToken(claims, user.getUserId());
    }
    public String generateToken(Map<String,Object> claims,String subject) {
        String jwtToken= Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256,"teamsecrectkey")
                .compact();
        System.out.println(jwtToken);
        return jwtToken;

}}
