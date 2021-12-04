package com.sanjai.config;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.BookingModel;

public class Createbookings {
 
	public List<BookingModel> createlistofbookings() {
		// TODO Auto-generated method stub
		System.out.println("bookings inside"); 
		Randomstringidgenerator r=new Randomstringidgenerator();
		List<BookingModel> bookings=new ArrayList<BookingModel>();
		String meals[]={"yes","no"};
		String travelclass[]={"business","economic"};
		for(int i=0;i<25;i++)
		{
			int x=r.generatenum(1, 50);
			BookingModel b=new BookingModel();
			 	b.setId("");
			    b.setUserid(x);
			    b.setName("user"+x);
			    b.setMeals(meals[r.generatenum(1,2)]);
			    b.setTickets(r.generatenum(1,4));
			    b.setTravelclass(travelclass[i%2]);
			    b.setFlight(r.generatenum(1,20));
			    b.setAmount(0);
			    LocalDate date = LocalDate.now();
			    date=date.plusDays(1);
			    b.setDateofbooking(date.toString());
			    b.setValidationstatus("valid");
			    b.setBookinglength(0);
			    bookings.add(b);
		}
		System.out.println("bookings created");
		return bookings;
	}

}
