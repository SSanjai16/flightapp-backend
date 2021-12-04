package com.sanjai.airplane;

import com.sanjai.config.Secure;
import com.sanjai.config.Token;

import database.Bookingcassandradb;
import database.Bookingdb;
import database.Citydb;
import database.Flightscassandradb;
import database.Userdb;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import model.BookingModel;
import model.Bookingclass;
import model.CityModel;
import model.FlightModel;
import model.Lengthmodel;
import model.UserModel;

@Path("flight-app")
public class FlightResources {
  public static String username = "admin";
  public static int prevpage=0; 
  public static int pagenum=0;
  public static int totalrecords=0;
  Bookingdb bookingdb = new Bookingdb();
  
  Userdb userdb = new Userdb();
  
  Bookingcassandradb cdb = new Bookingcassandradb();
  
  Citydb citydb = new Citydb();
  Flightscassandradb fdb =new Flightscassandradb();
  
  List<BookingModel> bookings = new ArrayList<>();
  
  @GET
  @Path("booking")
  @Secure
  @Produces({"application/json", "application/xml"})
  public List<BookingModel> getBooking() {
    //return this.bookingdb.returnpresentbooking();
    return this.cdb.returnpresentbookings();
  }
  
  @GET
  @Path("pastbooking")
  @Secure
  @Produces({"application/json", "application/xml"})
  public List<BookingModel> getPastBooking() {
    List<BookingModel> pastandinvalid = new ArrayList<>();
    pastandinvalid.addAll(this.cdb.retrievepastbookings());
    pastandinvalid.addAll(this.cdb.returninvalidbookings());
    //pastandinvalid.addAll(this.bookingdb.returninvalidbooking());
    return pastandinvalid;
  }
  @GET
  @Path("pastsearch")
  @Secure
  @Produces({"application/json", "application/xml"})
  public List<BookingModel> getbookingsfrompastsearch( @DefaultValue("null") @QueryParam("searchterm") String searchterm,
		  @DefaultValue("null") @QueryParam("searchclass") String searchclass,
		  @DefaultValue("null") @QueryParam("searchvalid") String searchvalid,
		  @DefaultValue("null") @QueryParam("searchmeal") String searchmeal ) {
	    //return this.bookingdb.allflights();
	  List<BookingModel> bookings =new ArrayList<BookingModel>();
	  List<BookingModel> temp =new ArrayList<BookingModel>();
	  System.out.println(searchterm);
	  System.out.println(searchclass); 
	  System.out.println(searchvalid);
	  System.out.println(searchmeal);
	  if(searchterm.equals("null") && searchclass.equals("null")&& searchvalid.equals("null")&& searchmeal.equals("null"))
	  {
		  System.out.println("h");
		  return this.cdb.getallbookings("past");
	  }
	  else if(searchclass.equals("null")&& searchvalid.equals("null")&& searchmeal.equals("null"))
	  {
		  System.out.println("hello");
		  return this.cdb.allsearch(searchterm,"past",searchclass,searchvalid,searchmeal);
	  }
	  else 
	  {
		  System.out.println("last else");
		  temp=cdb.categorysearch("pastbookflight",searchclass,searchvalid,searchmeal);
		  bookings.addAll(cdb.allsearch(searchterm,"past",searchclass,searchvalid,searchmeal));
		  
		  bookings.removeAll(temp);
		  bookings.addAll(temp); 
	  }
	  return bookings;
	  
  }
  @GET
  @Path("presentsearch")
  
  @Produces({"application/json", "application/xml"})
  public List<BookingModel> getbookingsfrompresentsearch(@QueryParam("searchterm") String searchterm,
		  @DefaultValue("null") @QueryParam("searchclass")String searchclass,
		  @DefaultValue("null")@QueryParam("searchvalid")String searchvalid,@DefaultValue("null") @QueryParam("searchmeal")String searchmeal) {
	  List<BookingModel> bookings =new ArrayList<BookingModel>();
	  List<BookingModel> temp =new ArrayList<BookingModel>();
	  System.out.println(searchterm);
	  System.out.println(searchclass); 
	  System.out.println(searchvalid);
	  System.out.println(searchmeal);
	  if(searchterm.equals("") && searchclass.equals("null")&& searchvalid.equals("null")&& searchmeal.equals("null"))
	  {
		  System.out.println("h");
		  return this.cdb.getallbookings("present");
	  }
	  else if(searchclass.equals("null")&& searchvalid.equals("null")&& searchmeal.equals("null"))
	  {
		  System.out.println("he");
		  return this.cdb.allsearch(searchterm,"present",searchclass,searchvalid,searchmeal);
	  }
	  else 
	  {
		 // System.out.println("last else");
		  bookings.addAll(cdb.allsearch(searchterm,"present",searchclass,searchvalid,searchmeal));
		  temp=cdb.categorysearch("presentbookflight",searchclass,searchvalid,searchmeal);
		  bookings.removeAll(temp);
		  bookings.addAll(temp);
	  } 
	  return bookings;
  }
  @GET
  @Path("manageflightsdata")
  @Secure
  @Produces({"application/json", "application/xml"})
  public List<FlightModel> getFlights() {
    //return this.bookingdb.allflights();
	  return this.fdb.allflights();
  }
  
  @GET
  @Path("cities")
  @Secure
  @Produces({"application/json", "application/xml"})
  public List<CityModel> getcities() {
    return this.citydb.getcities();
  }
  
  @POST
  @Path("flight")
//  @Secure
  @Consumes({"application/json", "application/xml"})
  public BookingModel createBooking(BookingModel b) throws Exception {
    if (b.getFlight() == 0 || b.getDateofbooking().equals("") || b.getTickets() == 0)
      throw new Exception("invalid details"); 
    System.out.println("createbooking-hi");
    Bookingclass book = new Bookingclass();
    b.setAmount(book.bookingamount(b));
    //this.bookingdb.create(b);
    return this.cdb.create(b);
    //return b;
  }
  
  @POST
  @Path("roundflight")
  @Secure
  @Consumes({"application/json", "application/xml"})
  public List<BookingModel> createroundBooking(List<BookingModel> roundbooking) {
    Bookingclass book = new Bookingclass();
    roundbooking = book.roundbookingamount(roundbooking);
    for (BookingModel b : roundbooking)
      System.out.println(b.getAmount()); 
    //sthis.bookingdb.createroundtrip(roundbooking);
    this.cdb.createroundtrip(roundbooking);
    return roundbooking;
  }
  
  @POST
  @Path("manageflights")
  @Secure
  @Consumes({"application/json", "application/xml"})
  public FlightModel createFlight(FlightModel f) throws Exception {
    if (f.getFlightnumber() == 0 || f.getEconomyclass() == 0 || f.getBusinessclass() == 0)
      throw new Exception("Invalid flight details"); 
    //this.bookingdb.createflight(f);
    return this.fdb.createflight(f);
    //return f;
  }
  
  @POST
  @Path("editflight")
  @Secure
  @Consumes({"application/json", "application/xml"})
  public FlightModel editFlight(FlightModel f) {
    //this.bookingdb.editflight(f);
    this.fdb.editflight(f);
    return f;
  }
  
  @POST
  @Path("deletebooking")
  @Secure
  @Produces({"application/json", "application/xml"})
  public BookingModel deleteBooking(BookingModel b) {
    //BookingModel b = this.bookingdb.getBooking(index);
    //this.bookingdb.delete(index);
	  System.out.println("delete");
    this.cdb.delete(b);
    return b;
  }
  
  @DELETE
  @Path("manageflights/{id}")
  @Secure
  public FlightModel deleteFlight(@PathParam("id") String index) {
	System.out.println("delete flight");
    FlightModel flight = this.fdb.getflight(index);
    //this.bookingdb.deleteflight(index);
    this.fdb.deleteflight(index);
    return flight;
  }
  @POST
  @Path("getflightfromcity")
  @Secure
  @Produces({"application/json"})
  public List<FlightModel> getflightfromcity(CityModel city) throws Exception {
	  return fdb.retrieveflightfromcity(city); 
	  
  }
  @POST
  @Path("getflightfromnumber/{number}")
  @Secure
  @Produces({"application/json"})
  public FlightModel getflightfromnumber(@PathParam("number") int number) throws Exception {
	  return fdb.getflightfromnumber(number);
	  
  }
  
  @POST
  @Path("addcity")
  @Secure
  @Produces({"application/json"})
  public CityModel addcity(CityModel city) throws Exception {
	  citydb.addcity(city);
	  return city;
	  
  }
  @GET
  @Path("getpastbookingsfrompage")
  @Secure
  @Produces({"application/json"})
  public List<BookingModel> getPastBookingfrompage(@QueryParam("pageindex") int pageindex,@QueryParam("pagesize") int pagesize) {
	  List<BookingModel> book=new ArrayList<BookingModel>();
	  long startTime= System.currentTimeMillis();
	  System.out.println("pageindex=>"+pageindex);
	  if(pageindex==0)
	  {
		  int x=cdb.lengthofbookings("past");
		  book=cdb.getbookingsbypage(pageindex,pagesize,x,"past");
	  }
	  else
		  book=cdb.getbookingsbypage(pageindex,pagesize,0,"past");
	  
	  
	  //book=cdb.getbookingsbypage(pageindex,pagesize,x,"past");
	  long endTime = System.currentTimeMillis();
	  System.out.println("That took " + (endTime - startTime) + " milliseconds");
	  return book;	
}
  @GET
  @Path("lengthofpresent")
  public Lengthmodel lengthofbookings()
  {
	  Lengthmodel l=new Lengthmodel();
	  l.setLengthofbookings(cdb.lengthofbookings("present"));
	  return l;
  }
  @GET
  @Path("getpresentbookingsfrompage")
  @Produces({"application/json"})
  public List<BookingModel> getPresentBookingfrompage(@QueryParam("pageindex") int pageindex
		  ,@QueryParam("pagesize") int pagesize) {
	  long startTime;
		long endTime;
		
	  	List<BookingModel> b=new ArrayList<BookingModel>();
	  	long startlength=System.currentTimeMillis();
	  		long endlength;
	  		endlength = System.currentTimeMillis(); 
			System.out.println("length function took " + (endlength - startlength) + " milliseconds");
			startTime= System.currentTimeMillis();
			if(pageindex==0)
			  {
				  int x=cdb.lengthofbookings("present");
				  b=cdb.getbookingsbypage(pageindex,pagesize,x,"present");
			  }
			else
				b= cdb.getbookingsbypage(pageindex,pagesize,0,"present");
				endTime = System.currentTimeMillis();
				System.out.println("get bookings in flightresources took " + (endTime - startTime) + " milliseconds");
	    return b; 
	   // pastandinvalid.addAll(this.cdb.retrievepastbookings());
	    //pastandinvalid.addAll(this.cdb.returninvalidbookings());
	   //return pastandinvalid; 
}
//  @GET
//  @Path("getlengthofpastbooking")
//  @Secure
//  @Produces({"application/json", "application/xml"})
//  public Lengthmodel getlength() {
//	  Lengthmodel l=new Lengthmodel();
//	  l= this.cdb.lengthofpastbookings();
//	  FlightResources.totalrecords=l.getLengthofbookings();
//	  return l;
//  }  
}