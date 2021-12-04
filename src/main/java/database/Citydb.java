package database;

import java.util.ArrayList;
import java.util.List;


import com.sanjai.airplane.FlightResources;
import com.sanjai.config.Cassandrautil;
import com.sanjai.config.Randomstringidgenerator;
import com.datastax.driver.core.*;
import model.BookingModel;
import model.CityModel;

public class Citydb {
	public List<CityModel> getcities()
	{
		System.out.println("inside cities");
		List <CityModel> cities=new ArrayList<CityModel>();
		List<Row> a=new ArrayList<Row>(); 
		try 
		{
			String query = "SELECT cityname FROM sanjai.cities";
	    	ResultSet result=Cassandrautil.getsession().execute(query);
	    	a=result.all();
	    	System.out.println("inside cities1");
	        for(Row r:a)
	        {
	        	CityModel c=new CityModel();
       	 	 	c.setCityname(r.getString("cityname"));
       	 	 	cities.add(c);
	        }
	        System.out.println(cities.size());
	    		 
		}
		catch(Exception e)
		{
			System.out.println("inside get cities " + e);
		}
		return cities;
	}
	public void addcity(CityModel c)
	{
		Randomstringidgenerator r=new Randomstringidgenerator();
		String id=r.generateid(7);
		try
		{
    	String query="insert into sanjai.cities (id,cityname)values(?,?);";
    	PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
		BoundStatement bound = prepared.bind(id,c.getCityname());
		Cassandrautil.getsession().execute(bound);
    	}
    	catch(Exception e)
    	{
    		System.out.println("create flight "+e);
    	}
	}
}
