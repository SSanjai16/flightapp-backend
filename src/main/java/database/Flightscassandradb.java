package database;

import java.util.ArrayList;
import java.util.List;
import java.util.*;
import com.sanjai.config.Cassandrautil;
import com.sanjai.config.Randomstringidgenerator;
import com.datastax.driver.core.*;
import model.CityModel;
import model.FlightModel;

public class Flightscassandradb {
	public FlightModel setflight(Row rs)
	{
		FlightModel f=new FlightModel();
 	   	f.setId(rs.getString("id"));
 	   	f.setBusinessclass(rs.getInt("businessclass"));
 	   	f.setDaysoftravel(rs.getString("daysoftravel")); 
 	   	f.setDestination(rs.getString("destination"));
 	   	f.setEconomyclass(rs.getInt("economyclass"));
 	   	f.setFlightnumber(rs.getInt("number"));
 	   	f.setSource(rs.getString("source"));
 	   	return f;
	}
	public List<FlightModel> allflights()
	{ 
    	List <FlightModel> flights=new ArrayList<FlightModel>();
    	List<Row> a=new ArrayList<Row>();
    	try
        {
		 	 String query = "SELECT * FROM sanjai.flights";
			 ResultSet result=Cassandrautil.getsession().execute(query);
			 System.out.println("Executed");
 		 a=result.all();
			 for(Row rs:a)
		        {
		      	   	flights.add(setflight(rs));
		        }
		
		 }
    	catch(Exception e)
        {
        	System.out.println("allflights" +e);
        }
	 System.out.println("executing");
	
	return flights;
	
	}
 public FlightModel createflight(FlightModel f)
	{
	 CustomMetric.average(f);
	 Randomstringidgenerator r=new Randomstringidgenerator();	    
	 CustomMetric.average("random",r.generateid());
    	try{
    		String x="";
    		x=r.generateid(3);
    		f.setId(x);
    	String query="insert into sanjai.flights (id,businessclass,daysoftravel,destination,economyclass,number,source)values(?,?,?,?,?,?,?);";
    	PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
		BoundStatement bound = prepared.bind(x,f.getBusinessclass(),f.getDaysoftravel(),f.getDestination(),f.getEconomyclass(),f.getFlightnumber(),f.getSource());
		Cassandrautil.getsession().execute(bound);
		System.out.println("inserted flight");
    	}
    	catch(Exception e)
    	{
    		System.out.println("create flight "+e);
    	}
    	return f;
	}
 
 private int getmaxflightid() {
 	int max=0;
		try
		{
			System.out.println("getmaxid");
		String query="select id from sanjai.flights limit 1;";
		ResultSet result=Cassandrautil.getsession().execute(query);
     System.out.println("Executed");
     Row a=result.one();
     
      max=a.getInt("id");
     }
		catch(Exception e)
		{
			System.out.println("getmaxflightid " + e );
		}
		return max;
	}
public void deleteflight(String index)
	{
	System.out.println("inside delete flight");
	Bookingcassandradb cdb=new Bookingcassandradb();
		int flightnum=0;
 	try
 	{
 		String q="select number from sanjai.flights where id=?";
 		PreparedStatement prepared = Cassandrautil.getsession().prepare(q);
	        BoundStatement bound = prepared.bind(index);
	        //result=session.execute(bound);
 		
 		ResultSet result=Cassandrautil.getsession().execute(bound);
      System.out.println("Executed");
      Row rs=result.one();
      flightnum=rs.getInt("number");
      String query="delete from sanjai.flights where id=?";
      PreparedStatement p = Cassandrautil.getsession().prepare(query);
	        BoundStatement b = p.bind(index);
	        Cassandrautil.getsession().execute(b);
	        //bdb.checkvalidation(flightnum);
	        System.out.println("inside delete flight done");
	        cdb.checkvalidation(flightnum);
 	}
 	catch(Exception ex){
			System.out.print("deleteflight " + ex);
		}
	}
public void editflight(FlightModel f)
	{
 	try
 	{
 		String query="update sanjai.flights set businessclass=?,economyclass=?,source=?,destination=?,daysoftravel=? where id=?;";
 		PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
 		checkvalidbooking(f);
	    BoundStatement bound = prepared.bind(f.getBusinessclass(),f.getEconomyclass(),f.getSource(),f.getDestination(),f.getDaysoftravel(),f.getId());
	    Cassandrautil.getsession().execute(bound);
      
 	}
 	catch(Exception e)
 	{
 		System.out.println("editflight "+e);
 	}
 	
	}
public List<FlightModel> retrieveflightfromcity(CityModel city) {
	List<FlightModel> flights=new ArrayList<FlightModel>();
	try
	{
		String query="select * from sanjai.flights where source=? allow filtering";
		PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
        BoundStatement bound = prepared.bind(city.getCityname());
        ResultSet result=Cassandrautil.getsession().execute(bound);
        List<Row> a=result.all();
        for(Row r:a)
        {
        	flights.add(setflight(r));	 	
        }
        String q="select * from sanjai.flights where destination=? allow filtering;";  
        PreparedStatement prepare = Cassandrautil.getsession().prepare(q);
        BoundStatement b = prepare.bind(city.getCityname());
        ResultSet rs=Cassandrautil.getsession().execute(b);
        List<Row> ar=rs.all();
        for(Row r:ar)
        {
        	flights.add(setflight(r));	 	
	}}
	catch(Exception e)
	{
		System.out.println("getflightfromcity "+e);
	}    
        
	return flights;
}
public void checkvalidbooking(FlightModel f)
{
	try
    {
		Bookingcassandradb cdb=new Bookingcassandradb();
		System.out.println("inside check valid" + f.getId());
		String query = "select * from sanjai.flights where id=?";
		PreparedStatement prepare = Cassandrautil.getsession().prepare(query);
        BoundStatement bound = prepare.bind(f.getId());
        ResultSet result=Cassandrautil.getsession().execute(bound);
        FlightModel oldflight = new FlightModel();
        Row a=result.one();
        oldflight.setId(a.getString("id"));
        oldflight.setBusinessclass(a.getInt("businessclass"));
        oldflight.setDaysoftravel(a.getString("daysoftravel"));
        oldflight.setDestination(a.getString("destination"));
        oldflight.setEconomyclass(a.getInt("economyclass"));
        oldflight.setFlightnumber(a.getInt("number"));
        oldflight.setSource(a.getString("source"));
        System.out.println(oldflight.getSource());
        System.out.println(oldflight.getDestination());
        if (!(oldflight.getDestination().equals(f.getDestination())) || !(oldflight.getSource().equals(oldflight.getSource())))
        {
        	System.out.println("s");
            cdb.setinvalidbooking(f.getFlightnumber());
        }
          if (oldflight.getDaysoftravel().equals("alldays") && f.getDaysoftravel().equals("weekends"))
          {
        	  System.out.println("a");
            cdb.checkandsetinvalidbooking(f.getFlightnumber(), "weekends");
          }
          if (oldflight.getDaysoftravel().equals("alldays") && f.getDaysoftravel().equals("weekdays")){
        	  System.out.println("n");
            cdb.checkandsetinvalidbooking(f.getFlightnumber(), "weekdays"); }
          if (oldflight.getDaysoftravel().equals("weekdays") && f.getDaysoftravel().equals("weekends"))
          {
        	  System.out.println("j");
            cdb.setinvalidbooking(f.getFlightnumber());
          }
          if (oldflight.getDaysoftravel() == "weekends" && f.getDaysoftravel() == "weekdays")
          {
        	  System.out.println("i");
            cdb.setinvalidbooking(f.getFlightnumber());
          }
         
      } catch (Exception e) {
        System.out.println("checkvalid" + e);
      } 	 
}
public FlightModel getflight(String index)
{
	  FlightModel f=new FlightModel();
	  try
	  {
		  String query = "select * from sanjai.flights where id=?";
		  PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
	      BoundStatement bound = prepared.bind(new Object[] { index });
	      ResultSet result = Cassandrautil.getsession().execute((Statement)bound);
	      Row a = (Row)result.one();
	      f.setBusinessclass(a.getInt("businessclass"));
	      f.setEconomyclass(a.getInt("economyclass"));
	      f.setDaysoftravel(a.getString("daysoftravel"));
	      f.setSource(a.getString("source"));
	      f.setDestination(a.getString("destination"));
	      f.setFlightnumber(a.getInt("number"));
	      f.setId(index);
	  }
	  catch (Exception e) {
	      System.out.println("get flight " + e);
	    } 
	  return f;
	 
}
public FlightModel getflightfromnumber(int number)
{
	  FlightModel f=new FlightModel();
	  try
	  {
		  String query = "select * from sanjai.flights where number=?";
		  PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
	      BoundStatement bound = prepared.bind(new Object[] { number });
	      ResultSet result = Cassandrautil.getsession().execute((Statement)bound);
	      Row a = (Row)result.one();
	      f.setBusinessclass(a.getInt("businessclass"));
	      f.setEconomyclass(a.getInt("economyclass"));
	      f.setDaysoftravel(a.getString("daysoftravel"));
	      f.setSource(a.getString("source"));
	      f.setDestination(a.getString("destination"));
	      f.setFlightnumber(a.getInt("number"));
	      f.setId(a.getString("id"));
	  }
	  catch (Exception e) {
	      System.out.println("get flight " + e);
	    } 
	  return f;
	 
}

}
