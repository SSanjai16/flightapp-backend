package com.sanjai.config;
import io.jsonwebtoken.Jwts;
import javax.crypto.KeyGenerator;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import java.util.Base64;
import java.security.Key;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.spec.SecretKeySpec;
public class Jwttokenutil {
	public String tokengenerator(String username,String password)
	{ 
		try
		{
		String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";
		System.out.println("inside token generator");
		String jwtToken = Jwts.builder()
		        .claim("username", username)
		        .claim("password",password) 
		        .signWith(SignatureAlgorithm.HS256,secret)
		        .setIssuedAt(new Date(System.currentTimeMillis()))
		        .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(10).toInstant()))
		        .compact();
		System.out.println(jwtToken);
		return jwtToken;
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return "";
		
	}
	

}
