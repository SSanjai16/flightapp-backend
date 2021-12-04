package com.sanjai.airplane;

import com.sanjai.config.Cassandrautil;
import com.sanjai.config.Jwttokenutil;
//import com.sanjai.config.RefreshPastBookings;
import com.sanjai.config.Token;
import database.Bookingcassandradb;
import database.Bookingdb;
import database.Userdb;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import model.BookingModel;
import model.UserModel;

@Path("user")
public class UserResource {
  Userdb userdb = new Userdb();
  
//  RefreshPastBookings rpb = new RefreshPastBookings(); 
//  
  Bookingcassandradb cdb = new Bookingcassandradb();
  
  Bookingdb bdb = new Bookingdb();
  @POST
  @Path("register")
  @Produces({"application/json", "application/xml"})
  public UserModel storeuser(UserModel user) throws Exception {
    String tempmail = user.getEmail();
    if (tempmail != null)
      if (this.userdb.fetchuserbymail(tempmail) != null) {
        System.out.println(tempmail);
        throw new Exception("user already exist");
      }  
    this.userdb.createuser(user);
    return user;
  }
  
  @POST
  @Path("login")
  @Produces({"application/json"})
  public Token checklogin(UserModel user) throws Exception {
    System.out.println("inside resource");
    UserModel u = null; 
    u = this.userdb.checklogin(user);
    System.out.println("inside login "+u.getEmail());
    if (u.getEmail() == null)
    {
    	System.out.println("it is null");
      throw new Exception("Wrong credentials");
    }
    Jwttokenutil jwt = new Jwttokenutil();
    FlightResources.username = user.getUsername();
    FlightResources.pagenum=0;
    FlightResources.prevpage=0;
    FlightResources.totalrecords=0;
    String jwttoken = jwt.tokengenerator(user.getUsername(), user.getPassword());
    Token T = new Token(); 
    T.token = jwttoken;
    //tr.runtests();
    //RefreshPastBookings.refreshdb();
    //Cassandrautil.getsession();
    return T;
  }
  @GET
  @Path("allusers")
  @Produces({"application/json"})
  public List<UserModel> getPresentBookingfrompage() {
	  System.out.println("allusers");
	  return userdb.getallusers(); 
  }
  @GET
  @Path("logout") 
 
  public Response logout() {
	  FlightResources.username="";
	  System.out.println("logging out");
	  return Response.status(200).build();
  }
  
}