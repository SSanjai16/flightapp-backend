package model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement ( name= "flight")
public class FlightModel {
	
	private String id;
	private int flightnumber; 
	private int businessclass;
	private int economyclass;
//	private int availablebusiness;
//	private int availableeconomy;
	private String source;
	private String destination; 
	private String daysoftravel;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getFlightnumber() {
		return flightnumber;
	}
	public void setFlightnumber(int flightnumber) {
		this.flightnumber = flightnumber;
	}
	public int getBusinessclass() {
		return businessclass;
	}
	public void setBusinessclass(int businessclass) {
		this.businessclass = businessclass;
	}
	public int getEconomyclass() {
		return economyclass;
	}
	public void setEconomyclass(int economyclass) {
		this.economyclass = economyclass;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getDaysoftravel() {
		return daysoftravel;
	}
	public void setDaysoftravel(String daysoftravel) {
		this.daysoftravel = daysoftravel;
	}

//	public int getAvailablebusiness() {
//		return availablebusiness;
//	}
//	public void setAvailablebusiness(int availablebusiness) {
//		this.availablebusiness = availablebusiness;
//	}
//	public int getAvailableeconomy() {
//		return availableeconomy;
//	}
//	public void setAvailableeconomy(int availableeconomy) {
//		this.availableeconomy = availableeconomy;
//	}
//	
	
	 

}
