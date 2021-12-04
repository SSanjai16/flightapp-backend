package model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "booking")
public class BookingModel {
	private String id; 
	private int userid;
	private String name;
	private int flight;
	private String meals;
	private String travelclass; 
	private int tickets;
	private int amount;
	private String dateofbooking; 
	private String validationstatus;
	private int bookinglength;
	public int getBookinglength() {
		return bookinglength;
	}
	public void setBookinglength(int bookinglength) {
		this.bookinglength = bookinglength;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMeals() {
		return meals;
	}
	public void setMeals(String meals) {
		this.meals = meals;
	}
	public int getTickets() {
		return tickets;
	}
	public void setTickets(int tickets) {
		this.tickets = tickets;
	}
	public String getTravelclass() {
		return travelclass;
	}
	public void setTravelclass(String travelclass) {
		this.travelclass = travelclass;
	}
	public int getFlight() {
		return flight;
	}
	public void setFlight(int flight) {
		this.flight = flight;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getDateofbooking()
	{
		return dateofbooking;
	}
	public void setDateofbooking(String dateofbooking)
	{
		this.dateofbooking=dateofbooking;
	}
	public String getValidationstatus() {
		return validationstatus;
	}
	public void setValidationstatus(String validationstatus) {
		this.validationstatus = validationstatus;
	}
	@Override
	public boolean equals(Object obj)
	{
		if(obj == this)
			return true;
		
		if(!(obj instanceof BookingModel))
		{
			return false;
		}
		
		BookingModel b = (BookingModel)obj;
		
		return b.id.equals(id);
		
	}

	
	
}
