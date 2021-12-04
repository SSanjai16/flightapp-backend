package com.sanjai.config;

import java.io.IOException;
import database.Userdb;
import java.security.Key;
import java.util.Base64;

import javax.annotation.Priority;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.sanjai.airplane.FlightResources;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
@Provider
@Secure
//@PreMatching
@Priority(Priorities.AUTHENTICATION)
 
public class Securedfilter implements ContainerRequestFilter {
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// TODO Auto-generated method stub
		//System.out.print("this is filter");
		Userdb u=new Userdb(); 
//		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
//
//        // Extract the token from the HTTP Authorization header
//        String token = authorizationHeader.substring("Bearer".length()).trim();
//        try
//        {
//        	Jwts.parser().setSigningKey(("asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4").getBytes()).parseClaimsJws(token);
//        }
//        catch(Exception e)
//        {
//        	requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
//        }
		//final HttpHeaders headers=requestContext.getHttpHeaders();
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        	//System.out.println("helllo");
        	String token=authorizationHeader.substring("Bearer".length()).trim();
        	//System.out.println(token);
        	try
          {
        		//System.out.println("hello");
        		Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode("asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4"), SignatureAlgorithm.HS256.getJcaName());
        		Claims claims=Jwts.parser()
                        .setSigningKey("asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4")
                        .parseClaimsJws(token)
                        .getBody();
        		 //FlightResources.username=(String) claims.get("username");
        		 String password=(String)claims.get("password");
        		 if(!u.checkloginvalid(FlightResources.username,password))
        		 {
        			 requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        		 }
        		
          }
        	
          catch(Exception e)
          {
        	  System.out.println("securefilter "+e);
          	requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
          }}
        else
    	{
        	System.out.println("wrong");
    		requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
    	}
        
	
	}
//            String authenticationToken = authorizationHeader.substring(7);
//            handleTokenBasedAuthentication(authenticationToken, requestContext);
//            return;
//        }
//
//        // Other authentication schemes (such as Basic) could be supported
//    }
//
//    private void handleTokenBasedAuthentication(String authenticationToken, ContainerRequestContext requestContext) {
//
//        AuthenticationTokenDetails authenticationTokenDetails = authenticationTokenService.parseToken(authenticationToken);
//        User user = userService.findByUsernameOrEmail(authenticationTokenDetails.getUsername());
//        AuthenticatedUserDetails authenticatedUserDetails = new AuthenticatedUserDetails(user.getUsername(), user.getAuthorities());
//
//        boolean isSecure = requestContext.getSecurityContext().isSecure();
//        SecurityContext securityContext = new TokenBasedSecurityContext(authenticatedUserDetails, authenticationTokenDetails, isSecure);
//        requestContext.setSecurityContext(securityContext);
    	//inal String encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");
    	 
    	//Decode username and password
    //	String usernameAndPassword;
   // 	try {
    //	    usernameAndPassword = new String(Base64.decode(encodedUserPassword));
    //	} catch (IOException e) {
    //	    return SERVER_ERROR;
    	//}
    //}
	}

