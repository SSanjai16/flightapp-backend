package com.sanjai.config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException; 
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServlet;

import database.Bookingcassandradb;
import model.BookingModel;
@WebListener
public class Postingdatascheduler implements ServletContextListener{
	private ScheduledExecutorService ses;
	 @Override
	 
	    public void contextInitialized(ServletContextEvent event) {
		 System.out.println("posting data");
		 	Createbookings c=new Createbookings();
		 	Bookingcassandradb bdb=new Bookingcassandradb();
		 	
			ses = Executors.newSingleThreadScheduledExecutor();
			 
			ses.scheduleAtFixedRate(new Runnable() { 
			    public void run() {
			    	List<BookingModel> bookings=new ArrayList<BookingModel>();
			    	System.out.println("inside run");
			    	bookings=c.createlistofbookings(); 
			    	System.out.println("size of new bookings" + bookings.size());
			    	bdb.insertlistofbookings(bookings);
			    	 
			    	}
			}, 0, 1, TimeUnit.HOURS); 

	 }
	 @Override
	    public void contextDestroyed(ServletContextEvent event) {
		 System.out.println("ending");
	        ses.shutdownNow();
	    }	}

