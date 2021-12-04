package com.sanjai.config;

import java.util.concurrent.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import database.Bookingcassandradb;
import database.Bookingdb;

import java.util.*; 
import java.io.*; 
import static java.util.concurrent.TimeUnit.*;
@WebListener
public class RefreshPastBookings implements ServletContextListener {
	private ScheduledExecutorService ses;
	@Override
	public void contextInitialized(ServletContextEvent event){
		System.out.println("inside refresh"); 
		Bookingcassandradb cdb=new Bookingcassandradb(); 
	ses = Executors.newSingleThreadScheduledExecutor();
	
	ses.scheduleAtFixedRate(new Runnable() {
	    public void run() {
	    	System.out.println("inside running");
	    	cdb.refreshpastbooking();
	    	}
	}, 0, 1, TimeUnit.HOURS);
	
} 
	@Override
    public void contextDestroyed(ServletContextEvent event) {
	 System.out.println("ending");
        ses.shutdownNow();
    }	
}





	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

//	private final ScheduledExecutorService scheduler =
//		     Executors.newScheduledThreadPool(1);
//
//		   public void beepForAnHour() {
//		     final Runnable beeper = new Runnable() {
//		       public void run() { System.out.println("beep"); }
//		     };
//		     final ScheduledFuture<?> beeperHandle =
//		       scheduler.scheduleAtFixedRate(beeper, 10, 10, SECONDS);
//		     scheduler.schedule(new Runnable() {
//		       public void run() { beeperHandle.cancel(true); }
//		     }, 60 * 60, SECONDS);
//		   }	
//}
//	
	
	
	
	
	
	
	
	
	
	
	
	
	





















//package com.sanjai.config;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//public class RefreshPastBookings {
//	private static final int MINUNTE = 1;
//	public static void refreshdb(){
//		while(true)
//		{
//			SimpleDateFormat bartDateFormat = new SimpleDateFormat("mm");
//            Date date = new Date();
//            int currentMin = new Integer(bartDateFormat.format(date))
//                    .intValue();
//            if (currentMin < MINUNTE) {
//                sleepMinutes(MINUNTE - currentMin);
//            } else if (currentMin > MINUNTE) {
//                sleepMinutes(60 - currentMin + MINUNTE);
//            } else {
//                // DO SOMETHING EVERY HOUR
//                System.out.println("come on do it!!!");
//                sleepMinutes(1);
//            }
//        }
//		}
//	private static void sleepMinutes(int minutes)
//	{
//		 try {
//	            System.out.println("Sleeping for " + minutes);
//	            Thread.sleep(minutes * 1000*60);
//	        } catch (InterruptedException e) {
//	            e.printStackTrace();
//	        }
//	}
//	
//	
//	
//	}
	
	


