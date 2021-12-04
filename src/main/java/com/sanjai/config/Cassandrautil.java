package com.sanjai.config;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class Cassandrautil {
	private static Session session=null; 
	public static Session getsession()
	{
		if(session==null)
		{
			Cluster cluster = null;
			cluster = Cluster.builder().addContactPoint("127.0.0.4").withPort(9042).build();
		    System.out.println(" s");    
		    session = cluster.connect();                                          

//			List<InetSocketAddress> contactpoints=new ArrayList<InetSocketAddress>();
//			contactpoints.add(new InetSocketAddress("127.0.0.1",9042));
//			contactpoints.add(new InetSocketAddress("127.0.0.6",9042));
//			System.out.println(contactpoints);
			//session=CqlSession.builder().addContactPoints(contactpoints).withLocalDatacenter("dc1").build();
//			 session = (CqlSession)((CqlSessionBuilder)((CqlSessionBuilder)CqlSession.builder().addContactPoints(contactpoints)).withLocalDatacenter("dc1")).build();
			//session=CqlSession.builder().addContactPoint(new InetSocketAddress("127.0.0.5",9042)).build();
			//session=CqlSession.builder().addContactPoint(new InetSocketAddress("cassandra_db",9042)).build();
			//session=CqlSession.builder().addContactPoint(new InetSocketAddress("cassandra_db",9042)).withLocalDatacenter("datacenter1").build();
			System.out.println("built");
		}
		return session;   
	} 
	public static void endsession()
	{
		session.close();
		session=null;
	}
}
