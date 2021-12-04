/**
 * 
 */
package com.sanjai.airplane;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;



import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class FlightResourceTest {
	String token="";
	String tempid="";
	public Map<String,String> headers()
	{
		Map<String, String> headers = new HashMap<String,String>();
		headers.put("Accept","application/json");
		headers.put("Authorization","Bearer "+token);
		return headers;
	}
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	@Before
	public void setUp() throws Exception {
		String payload="{\r\n"+
				" \"username\": \"admin\",\r\n"+
				" \"password\"  : \"admin\"\r\n"+
				"}";
		token=given()
			  .contentType(ContentType.JSON).
			  body(payload)
			  .when()
			  .post("http://localhost:8007/airplane/webapi/user/login")
			  .jsonPath()
			  .get("token");
		System.out.println(token);
	}

	
	@After
	public void tearDown() throws Exception {
		token="";
		
	}
	@Test
	public void testGetFlights() {
		
		given().headers(headers()).when().get("http://localhost:8007/airplane/webapi/flight-app/manageflightsdata").then().assertThat().statusCode(HttpStatus.SC_OK);
	}

	@Test
	public void testGetcities() {
		given().headers(headers()).when().get("http://localhost:8007/airplane/webapi/flight-app/cities").then().assertThat().statusCode(HttpStatus.SC_OK);
	}

	
	@Test
	public void testGetflightfromcity() {
		
	}

	
	@Test
	public void testGetPastBookingfrompage() {
		given().headers(headers()).when().get("http://localhost:8007/airplane/webapi/flight-app/getpastbookingsfrompage?pagesize=50&pageindex=0").then().assertThat().statusCode(HttpStatus.SC_OK);
	}

	



	@Test
	public void testGetPresentBookingfrompage() {
		given().headers(headers()).when().get("http://localhost:8007/airplane/webapi/flight-app/getpresentbookingsfrompage?pagesize=50&pageindex=0").then().assertThat().statusCode(HttpStatus.SC_OK);
	}
	
	public String testCreateBooking() {
		LocalDate date = LocalDate.now();
		String d=date.toString();
		String tempbooking="{\"flight\":1,\"amount\":0,\"bookinglength\":0,\"tickets\":2,\"dateofbooking\":\""+d+ "\",\"name\":\"admin\",\"travelclass\":\"business\",\"validationstatus\":\"valid\",\"id\":\"\",\"userid\":0,\"meals\":\"yes\"}";
		System.out.println(tempbooking);
		String id=given().contentType(ContentType.JSON).headers(headers()).body(tempbooking)
		.when()
		.post("http://localhost:8007/airplane/webapi/flight-app/flight")
		.jsonPath()
		  .get("id");
		System.out.println(id);
		return id;
	}
	@Test
	public void testDeleteBooking(){
		String id=testCreateBooking();
		LocalDate date = LocalDate.now();
		String d=date.toString();
		String tempbooking="{\"flight\":1,"
				+ "\"amount\":0,"
				+ "\"bookinglength\":0,"
				+ "\"tickets\":1,"
				+ "\"dateofbooking\":\""+d+"\","
				+ "\"name\":\"admin\","
				+ "\"travelclass\":\"business\","
				+ "\"validationstatus\":\"valid\","
				+ "\"id\":\""+id+"\","
				+ "\"userid\":0,"
				+ "\"meals\":\"yes\"}";
		System.out.println(tempbooking);
		given().contentType(ContentType.JSON)
		.headers(headers())
		.body(tempbooking)
		.when()
		.post("http://localhost:8007/airplane/webapi/flight-app/deletebooking")
		.then()
		.assertThat().body("id",equalTo(id));	
	}
	public String testCreateFlight() {
		String tempflight="{\"flightnumber\":100,\"destination\":\"Mumbai\",\"businessclass\":300,\"economyclass\":300,\"id\":\"\",\"source\":\"Chennai\",\"daysoftravel\":\"weekdays\"}";
		System.out.println(tempflight);
		String id=given().contentType(ContentType.JSON).headers(headers()).body(tempflight)
		.when()
		.post("http://localhost:8007/airplane/webapi/flight-app/manageflights")
		.jsonPath()
		  .get("id");
		System.out.println(id);
		return id;
	}
	@Test
	public void testDeleteFlight(){
		String id=testCreateFlight();
		String tempflight="{\"flightnumber\":100,\"destination\":\"Mumbai\",\"businessclass\":300,\"economyclass\":300,\"id\":\""+id+ "\",\"source\":\"Chennai\",\"daysoftravel\":\"weekdays\"}";
		System.out.println(tempflight);
		given().contentType(ContentType.JSON)
		.headers(headers())
		.when()
		.delete("http://localhost:8007/airplane/webapi/flight-app/manageflights"+"/"+id)
		.then()
		.assertThat().statusCode(HttpStatus.SC_OK);	
	}
	
	

}
