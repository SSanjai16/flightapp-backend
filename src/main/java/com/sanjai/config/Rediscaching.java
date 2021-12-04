package com.sanjai.config;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.BookingModel;
import redis.clients.jedis.Jedis;
public class Rediscaching {

	public void setinredis(int pageindex, String pagestate) {
		// TODO Auto-generated method stub
		
		try
		{
			Jedis jedis=new Jedis("localhost");
			//System.out.println("connected" + jedis.ping());
			if(!(pagestate.equals(""))) 
			{ 
				jedis.set(Integer.toString(pageindex), pagestate);
			}
			else 
			{
				jedis.set(Integer.toString(pageindex),"null");
			}
			jedis.close();
   	    }
		catch(Exception e) 
		{
			System.out.println("setinradis "+e);
	     }
		
		
	}
	public String getfromradis(int pageindex)
	{
		String pagestate="";
		try
		{
			Jedis jedis=new Jedis("localhost");
			//System.out.println("connected" + jedis.ping());
			pagestate = jedis.get(Integer.toString(pageindex));
			//System.out.println(pagestate);	
			jedis.close();
		}
		catch(Exception e)
		{
			System.out.println("getfromradis "+e);
	     }
		return pagestate;

	}
	public void setbookingsfornext4page(List<BookingModel> bookingsforredis,int pagenum) {
		if(bookingsforredis.size()!=0)
		{
		try
		{
			long startTime= System.currentTimeMillis();
			Jedis jedis=new Jedis("localhost");
			//System.out.println("connectedsetb4" + jedis.ping());
			int i=1;
			for(BookingModel b:bookingsforredis)
			{
				//int i=1;
				//System.out.print(i+" ");
				String rowname="row"+pagenum+i;
				jedis.hset(rowname,"id",b.getId());
				jedis.hset(rowname,"userid",Integer.toString(b.getUserid()));
				jedis.hset(rowname,"name",b.getName());
				jedis.hset(rowname,"mealpref",b.getMeals());
				jedis.hset(rowname,"tickets",Integer.toString(b.getTickets()));
				jedis.hset(rowname,"class",b.getTravelclass());
				jedis.hset(rowname,"flightnum",Integer.toString(b.getFlight()));
				jedis.hset(rowname,"amount",Integer.toString(b.getAmount()));
				
				jedis.hset(rowname,"dateofbooking",b.getDateofbooking());
				
				jedis.hset(rowname,"validationstatus",b.getValidationstatus());
				i++;
			}
			long endTime = System.currentTimeMillis();

			System.out.println("redis setbookings for 4 page  " + (endTime - startTime) + " milliseconds");

		} 
		catch(Exception e)
		{
			System.out.println("setbookings "+e);
	     }
		}
		
	}
	public List<BookingModel> getbookingsforpage(int currentpage,int pagesize) {
		// TODO Auto-generated method stub
		List<BookingModel> bookings=new ArrayList<BookingModel>();
		
		try
		{
			if(currentpage==0)
				currentpage=4;
			Jedis jedis=new Jedis("localhost");
			//System.out.println("connected" + jedis.ping());
			long startTime= System.currentTimeMillis();
			for(int i=1;i<=pagesize;i++)
			{
				BookingModel b=new BookingModel();
				String rowname="row"+currentpage+i;
				b.setId(jedis.hget(rowname,"id"));
				b.setUserid(Integer.parseInt(jedis.hget(rowname,"userid")));
				b.setName(jedis.hget(rowname,"name"));
				b.setMeals(jedis.hget(rowname,"mealpref"));
				b.setTickets(Integer.parseInt(jedis.hget(rowname,"tickets")));
				b.setTravelclass(jedis.hget(rowname,"class"));
				b.setFlight(Integer.parseInt(jedis.hget(rowname,"flightnum")));
				b.setAmount(Integer.parseInt(jedis.hget(rowname,"amount")));
				
				b.setDateofbooking(jedis.hget(rowname,"dateofbooking")); 
				b.setValidationstatus(jedis.hget(rowname,"validationstatus"));
				bookings.add(b);
			}
			long endTime = System.currentTimeMillis();

			System.out.println("getbookings for 4 page from redis class " + (endTime - startTime) + " milliseconds");

		}
		catch(Exception e)
		{
			System.out.println("getbookings"+e);
	     }

		return bookings;  
	}

}
