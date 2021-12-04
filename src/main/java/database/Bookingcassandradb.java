package database;
import com.datastax.driver.core.*;
import com.sanjai.airplane.FlightResources;
import com.sanjai.config.Cassandrautil;
import com.sanjai.config.Randomstringidgenerator;
import com.sanjai.config.Rediscaching;
import java.nio.ByteBuffer;
//import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
//import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.BookingModel;
import model.Bookingclass;
import model.FlightModel;
public class Bookingcassandradb {
  public String insertstringbuilder(String tablename) {
    if (tablename.equals("presentbookflight"))
      return "insert into sanjai.presentbookflight(id,userid,name,mealpref,tickets,class,flightnum,amount,dateofbooking,validationstatus) values(?,?,?,?,?,?,?,?,?,?) USING TTL 604800"; 
    if (tablename.equals("pastbookflight"))
      return "insert into sanjai.presentbookflight(id,userid,name,mealpref,tickets,class,flightnum,amount,dateofbooking,validationstatus) values(?,?,?,?,?,?,?,?,?,?) USING TTL 604800"; 
    return ""; 
  }
  
  public BookingModel setbooking(Row r, int len) {
    BookingModel b = new BookingModel();
    b.setId(r.getString("id"));
    b.setUserid(r.getInt("userid")); 
    b.setName(r.getString("name"));
    b.setMeals(r.getString("mealpref"));
    b.setTickets(r.getInt("tickets"));
    b.setTravelclass(r.getString("class"));
    b.setFlight(r.getInt("flightnum"));
    b.setAmount(r.getInt("amount"));
    //ystem.out.println("dates above");
    Date date = r.getTimestamp("dateofbooking");
   // System.out.println("date below");
    b.setDateofbooking(new SimpleDateFormat("MM/dd/yyyy").format(date));
    //b.setDateofbooking("");
    //System.out.println("date above2");
    b.setValidationstatus(r.getString("validationstatus"));
    b.setBookinglength(len);
    return b;
  }
  
  public void refreshpastbooking() {
    System.out.println("inside1 refreshpastbooking");
    String query = "select * from sanjai.presentbookflight where dateofbooking<dateOf(now()) allow filtering";
    List<Row> a = new ArrayList<>();
    List<BookingModel> booking = new ArrayList<>();
    try {
      System.out.println("inside2 refreshpastbooking");
      ResultSet result = Cassandrautil.getsession().execute(query);
      a = result.all();
      System.out.println(a);
      for (Row r : a) {
        System.out.println("insidee refreshpastbooking");
        booking.add(setbooking(r, 0));
      } 
      System.out.println("outside for");
      insertpastbookingstocassandra(booking);
      deletepastbookings(booking);
    } catch (Exception E) {
      System.out.println("refreshpastbooking " + E);
    } 
  }
  
  public void deletepastbookings(List<BookingModel> booking) {
    String query = "delete from sanjai.presentbookflight where id=?";
    try {
      for (BookingModel b : booking) {
        PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
        BoundStatement bound = prepared.bind(new Object[] { b.getId() });
        Cassandrautil.getsession().execute((Statement)bound);
      } 
    } catch (Exception E) {
      System.out.println("deletepastbooking " + E);
    } 
    System.out.println("deleted");
  }
  
  public void insertpastbookingstocassandra(List<BookingModel> list) {
    System.out.println("list " + list);
    try {
      String query = "insert into sanjai.pastbookflight(id,userid,name,mealpref,tickets,class,flightnum,amount,dateofbooking,validationstatus) values(?,?,?,?,?,?,?,?,?,?) USING TTL 604800";
      for (BookingModel b : list) {
        System.out.println("inside casandra for");
        LocalDate localDate = LocalDate.parse(b.getDateofbooking());
        Timestamp timestamp = Timestamp.valueOf(localDate.atStartOfDay());
        PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
        BoundStatement bound = prepared.bind(new Object[] { b.getId(), Integer.valueOf(b.getUserid()), b.getName(), b.getMeals(), Integer.valueOf(b.getTickets()), b.getTravelclass(), Integer.valueOf(b.getFlight()), Integer.valueOf(b.getAmount()),timestamp, b.getValidationstatus() });
        Cassandrautil.getsession().execute((Statement)bound);
      } 
    } catch (Exception E) {
      System.out.println("except " + E);
    } 
    System.out.println("inserting");
  }
  
  public List<BookingModel> retrievepastbookings() {
    List<Row> a = new ArrayList<>();
    List<BookingModel> booking = new ArrayList<>();
    try {
      ResultSet result;
      long startTime = System.currentTimeMillis();
      String query = "";
      if (FlightResources.username.equals("admin")) {
        query = "SELECT * FROM sanjai.pastbookflight";
        result = Cassandrautil.getsession().execute(query);
      } else {
        query = "select * from sanjai.pastbookflight where name=? allow filtering";
        PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
        BoundStatement bound = prepared.bind(new Object[] { FlightResources.username });
        result = Cassandrautil.getsession().execute((Statement)bound);
      } 
      System.out.println("Executed");
      a = result.all();
      for (Row r : a)
        booking.add(setbooking(r, 0)); 
      long endTime = System.currentTimeMillis();
      System.out.println("retrieve pastbooking took " + (endTime - startTime) + " milliseconds");
    } catch (Exception e) {
      System.out.println("retrieve pastbooking exception " + e);
    } 
    return booking;
  }
  
  public List<BookingModel> returnpresentbookings() {
    long startTime = System.currentTimeMillis();
    List<Row> a = new ArrayList<>();
    List<BookingModel> booking = new ArrayList<>();
    try {
      ResultSet result;
      String query = "";
      if (FlightResources.username.equals("admin")) {
        query = "SELECT * FROM sanjai.presentbookflight";
        result = Cassandrautil.getsession().execute(query);
      } else {
        query = "select * from sanjai.presentbookflight where name=? allow filtering";
        PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
        BoundStatement bound = prepared.bind(new Object[] { FlightResources.username });
        result = Cassandrautil.getsession().execute((Statement)bound);
      } 
      System.out.println("Executed");
      a = result.all();
      for (Row r : a)
        booking.add(setbooking(r, 0)); 
      long endTime = System.currentTimeMillis();
      System.out.println("return present bookings took " + (endTime - startTime) + " milliseconds");
    } catch (Exception e) {
      System.out.println("return present exception" + e);
    } 
    System.out.println("executing");
    return booking;
  }
  
  public List<BookingModel> returninvalidbookings() {
    List<Row> a = new ArrayList<>();
    List<BookingModel> booking = new ArrayList<>();
    try {
      ResultSet result;
      String query = "";
      if (FlightResources.username.equals("admin")) {
        query = "SELECT * FROM sanjai.presentbookflight where validationstatus='invalid' allow filtering";
        result = Cassandrautil.getsession().execute(query);
      } else {
        query = "select * from sanjai.presentbookflight where name=? and validationstatus='invalid' allow filtering";
        PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
        BoundStatement bound = prepared.bind(new Object[] { FlightResources.username });
        result = Cassandrautil.getsession().execute((Statement)bound);
      } 
      System.out.println("returninvalidbooking");
      a = result.all();
      for (Row r : a)
        booking.add(setbooking(r, 0)); 
    } catch (Exception e) {
      System.out.println("returninvalidbooking" + e);
    } 
    System.out.println("executing");
    return booking;
  }
  
  public BookingModel create(BookingModel b) {
    try {
      Bookingdb bdb = new Bookingdb();
      Randomstringidgenerator r = new Randomstringidgenerator();
      int currentuserid = bdb.getuserid(FlightResources.username);
      String query = "insert into sanjai.presentbookflight(id,userid,name,mealpref,tickets,class,flightnum,amount,dateofbooking,validationstatus) values(?,?,?,?,?,?,?,?,?,?) USING TTL 604800";
      LocalDate localDate = LocalDate.parse(b.getDateofbooking());
      Timestamp timestamp = Timestamp.valueOf(localDate.atStartOfDay());
      PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
      String id = r.generateid(7);
      b.setId(id);
      BoundStatement bound = prepared.bind(new Object[] { id, Integer.valueOf(currentuserid), b.getName(), b.getMeals(), Integer.valueOf(b.getTickets()), b.getTravelclass(), Integer.valueOf(b.getFlight()), Integer.valueOf(b.getAmount()), timestamp, b.getValidationstatus() });
      Cassandrautil.getsession().execute((Statement)bound);
      System.out.println("inserted successfully");
    } catch (Exception E) {
      System.out.println("create " + E);
    } 
    return b;
  }
  public void insertlistofbookings(List<BookingModel> bookings) {
		// TODO Auto-generated method stub
	  try {
		  System.out.println("inside cassandra");
		  System.out.println(bookings);
	      Bookingdb bdb = new Bookingdb();
	      Bookingclass book=new Bookingclass();
	      Randomstringidgenerator r = new Randomstringidgenerator();
	      String query = "insert into sanjai.presentbookflight(id,userid,name,mealpref,tickets,class,flightnum,amount,dateofbooking,validationstatus) values(?,?,?,?,?,?,?,?,?,?) USING TTL 604800";
	      for(BookingModel b: bookings)
	      {
	      LocalDate localDate = LocalDate.parse(b.getDateofbooking());
	      Timestamp timestamp = Timestamp.valueOf(localDate.atStartOfDay());
	      PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
	      String id = r.generateid(7);
	      b.setAmount(book.bookingamount(b));
	      BoundStatement bound = prepared.bind(new Object[] { id,b.getUserid(), b.getName(), b.getMeals(), Integer.valueOf(b.getTickets()), b.getTravelclass(), Integer.valueOf(b.getFlight()), Integer.valueOf(b.getAmount()),timestamp, b.getValidationstatus() });
	      Cassandrautil.getsession().execute(bound);
	      System.out.println("bookings inserted"); 
	      }
	    } catch (Exception E) { 
	      System.out.println("create " + E);
	    } 
		
		
	}
  
  public void createroundtrip(List<BookingModel> rb) {
    try {
      String id = "";
      Bookingdb bdb = new Bookingdb();
      Randomstringidgenerator r = new Randomstringidgenerator();
      int currentuserid = bdb.getuserid(FlightResources.username);
      for (BookingModel b : rb) {
        String query = "insert into sanjai.presentbookflight(id,userid,name,mealpref,tickets,class,flightnum,amount,dateofbooking,validationstatus) values(?,?,?,?,?,?,?,?,?,?) USING TTL 604800";
        LocalDate localDate = LocalDate.parse(b.getDateofbooking());
        Timestamp timestamp = Timestamp.valueOf(localDate.atStartOfDay());
        PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
        id = r.generateid(7);
        BoundStatement bound = prepared.bind(new Object[] { id, Integer.valueOf(currentuserid), b.getName(), b.getMeals(), Integer.valueOf(b.getTickets()), b.getTravelclass(), Integer.valueOf(b.getFlight()), Integer.valueOf(b.getAmount()), timestamp, b.getValidationstatus() });
        Cassandrautil.getsession().execute((Statement)bound);
      } 
    } catch (Exception E) {
      System.out.println("createroundtrip " + E);
    } 
  }
  
  public BookingModel getBooking(String index) {
    BookingModel booking = new BookingModel();
    try {
      String query = "select * from sanjai.presentbookflight where id=? allow filtering";
      PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
      BoundStatement bound = prepared.bind(new Object[] { index });
      ResultSet result = Cassandrautil.getsession().execute((Statement)bound);
      Row a = (Row)result.one();
      booking = setbooking(a, 0);
    } catch (Exception E) {
      System.out.println("getbooking " + E);
    } 
    return booking;
  }
  
  public List<BookingModel> pastsearchbookings(int flightnum) {
    List<Row> a = new ArrayList<>();
    List<BookingModel> booking = new ArrayList<>();
    try {
      ResultSet result;
      String query = "";
      if (FlightResources.username.equals("admin")) {
        query = "SELECT * FROM sanjai.pastbookflight where flightnum=? allow filtering";
        PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
        BoundStatement bound = prepared.bind(new Object[] { Integer.valueOf(flightnum) });
        result = Cassandrautil.getsession().execute((Statement)bound);
      } else {
        query = "SELECT * FROM sanjai.pastbookflight where flightnum=? and name=? allow filtering";
        PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
        BoundStatement bound = prepared.bind(new Object[] { Integer.valueOf(flightnum), FlightResources.username });
        result = Cassandrautil.getsession().execute((Statement)bound);
      } 
      System.out.println("Executed");
      a = result.all();
      for (Row r : a)
        booking.add(setbooking(r, 0)); 
    } catch (Exception e) {
      System.out.println("past search exception" + e);
    } 
    System.out.println("executing");
    return booking;
  }
  
  public void delete(BookingModel b) {
    try {
    	
      System.out.println("id" + b.getId());
      String query = "delete from sanjai.presentbookflight where id=?";
      PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
      BoundStatement bound = prepared.bind(new Object[] { b.getId() });
      Cassandrautil.getsession().execute((Statement)bound);
    } catch (Exception e) {
      System.out.println("delete flight " + e);
    } 
  }
  
  
  public List<BookingModel> getBookingsbyflight(int flightnum) {
    List<Row> a = new ArrayList<>();
    List<BookingModel> booking = new ArrayList<>();
    try {
      String query = "";
      query = "select * from sanjai.presentbookflight where flightnum=? and validationstatus='valid' allow filtering";
      PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
      BoundStatement bound = prepared.bind(new Object[] { Integer.valueOf(flightnum) });
      ResultSet result = Cassandrautil.getsession().execute((Statement)bound);
      System.out.println("Executed");
      a = result.all();
      for (Row r : a)
        booking.add(setbooking(r, 0)); 
    } catch (Exception e) {
      System.out.println("get bookingsby flight exception" + e);
    } 
    System.out.println("executing");
    return booking;
  }
  
  public void checkvalidation(int flightnum) {
    try {
      String query = "update sanjai.presentbookflight set validationstatus='invalid' where id=?;";
      List<BookingModel> bookings = new ArrayList<>();
      bookings.addAll(getBookingsbyflight(flightnum));
      for (BookingModel b : bookings) {
        PreparedStatement prepare = Cassandrautil.getsession().prepare(query);
        BoundStatement bound = prepare.bind(new Object[] { b.getId() });
        Cassandrautil.getsession().execute((Statement)bound);
      } 
      System.out.println("done "+flightnum);
    } catch (Exception e) {
      System.out.println("checkvalidation " + e);
    } 
  }
  
  public void setinvalidbooking(int flightnumber) {
    List<BookingModel> bookings = new ArrayList<>();
    System.out.println("setinvalidbooking");
    bookings.addAll(getBookingsbyflight(flightnumber));
    String query = "Update sanjai.presentbookflight set validationstatus='invalid' where id=?";
    try {
      for (BookingModel b : bookings) {
        PreparedStatement prepare = Cassandrautil.getsession().prepare(query);
        BoundStatement bound = prepare.bind(new Object[] { b.getId() });
        Cassandrautil.getsession().execute((Statement)bound);
      } 
    } catch (Exception e) {
      System.out.println("setinvalid " + e);
    } 
  }
  
  public void checkandsetinvalidbooking(int flightnum, String dayoftravel) {
    System.out.println("checkandsetinvalidbooking");
    Bookingclass bc = new Bookingclass();
    List<BookingModel> bookings = new ArrayList<>();
    bookings.addAll(getBookingsbyflight(flightnum));
    try {
      for (BookingModel b : bookings) {
        if (dayoftravel == "weekends" && 
          !bc.checkweekend(b.getDateofbooking())) {
          String query1 = "update sanjai.presentbookflight set validationstatus='invalid' where id=?";
          PreparedStatement prepare = Cassandrautil.getsession().prepare(query1);
          BoundStatement bound = prepare.bind(new Object[] { b.getId() });
          Cassandrautil.getsession().execute((Statement)bound);
        } 
        if (dayoftravel == "weekdays" && 
          bc.checkweekend(b.getDateofbooking())) {
          String query1 = "update sanjai.presentbookflight set validationstatus='invalid' where id=?";
          PreparedStatement prepare = Cassandrautil.getsession().prepare(query1);
          BoundStatement bound = prepare.bind(new Object[] { b.getId() });
          Cassandrautil.getsession().execute((Statement)bound);
        } 
      } 
    } catch (Exception e) {
      System.out.println("checkandsetinvalid " + e);
    } 
  }
  
  public List<BookingModel> getbookingsbypage(int pageindex, int pagesize, int len, String page) {
	  List<BookingModel> bookingsforredis = new ArrayList<>();
	    List<BookingModel> bookings = new ArrayList<>();
	    String pagestatus = "";
	    ResultSet rs = null;
	    PagingState pagingstate = null;
	    String pagestate = "";
	    if (pageindex < FlightResources.prevpage) {
	      pagestatus = "previouspage";
	    } else {
	      pagestatus = "nextpage";
	    } 
	    FlightResources.prevpage = pageindex;
	    Rediscaching rc = new Rediscaching();
	    String query = "";
	    if (pageindex <= FlightResources.pagenum && pagestatus.equals("nextpage") && pageindex != 0) {
	      System.out.println("inside if");
	      bookings.clear();
	      long starttime = System.currentTimeMillis();
	      bookings = rc.getbookingsforpage(pageindex % 4, pagesize);
	      long endTime = System.currentTimeMillis();
	      System.out.println("next page from redis took " + (endTime - starttime) + " milliseconds");
	      return bookings;
	    } 
	    if (pagestatus.equals("previouspage") && pageindex > FlightResources.pagenum - 4 && pageindex != 0)
	      return rc.getbookingsforpage(pageindex % 4, pagesize); 
	    if (pageindex == 0) {
	      try {
	        FlightResources.pagenum = 0;
	        if (FlightResources.username.equals("admin")) {
	          if (page.equals("past")) {
	            query = "select * from sanjai.pastbookflight";
	            System.out.println("past admin");
	          } else {
	            query = "select * from sanjai.presentbookflight";
	            System.out.println("present admin");
	          } 
	         Statement statement =new SimpleStatement(query).setFetchSize(pagesize);
	         System.out.println("outside statement");
	          rs = Cassandrautil.getsession().execute((Statement)statement);
	          System.out.println("outside statement out");
	        } else {
	          if (page.equals("past")) {
	            query = "select * from sanjai.pastbookflight where name like ? allow filtering";
	            
	          } else {
	            query = "select * from sanjai.presentbookflight where name like ? allow filtering";
	            System.out.println("inside present else");
	          } 
	          System.out.println("above prepared");
	          PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
	          BoundStatement statementBuilder =prepared.bind(); 
	          statementBuilder.bind(FlightResources.username).setFetchSize(pagesize);
	          rs = Cassandrautil.getsession().execute((Statement)statementBuilder);
	        } 
	        System.out.println("pagingstate above");
	        pagingstate = rs.getExecutionInfo().getPagingState();
	        System.out.println("pagingstate below");
	        if(pagingstate!=null)
	        	pagestate = pagingstate.toString();
	        System.out.println("pagestate below");
	        long startTime = System.currentTimeMillis();
	        System.out.println("pagestate 2 below");
	        while (rs.getAvailableWithoutFetching() > 0) {
	          Row r = (Row)rs.one();
	          bookings.add(setbooking(r, len));
	        } 
	        System.out.println("pagingstate above");
	        long endTime = System.currentTimeMillis();
	        System.out.println("while took " + (endTime - startTime) + " milliseconds");
	        bookingsforredis.clear();
	        startTime = System.currentTimeMillis();
	        for (int i = 1; i <= 4; i++) {
	          FlightResources.pagenum++;
	          if (FlightResources.username.equals("admin")) {
	            if (page.equals("past")) {
	              query = "select * from sanjai.pastbookflight";
	            } else {
	              query = "select * from sanjai.presentbookflight";
	            } 
	            System.out.println("below if");
	            Statement statement = new SimpleStatement(query).setFetchSize(pagesize).setPagingState(pagingstate);
	            System.out.println("below statement");
	            rs = Cassandrautil.getsession().execute((Statement)statement);
	            System.out.println("below rs");
	          } else {
	            if (page.equals("past")) {
	              query = "select * from sanjai.pastbookflight where name like ? allow filtering";
	            } else {
	              query = "select * from sanjai.presentbookflight where name like ? allow filtering";
	            } 
	            PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
	            BoundStatement statementBuilder = prepared.bind();
	             statementBuilder.bind(FlightResources.username).setPagingState(pagingstate).setFetchSize(pagesize);
	            rs = Cassandrautil.getsession().execute((Statement)statementBuilder);
	          } 
	          pagingstate = rs.getExecutionInfo().getPagingState();
	          if(pagingstate!=null)
	        	  pagestate = pagingstate.toString();
	          while (rs.getAvailableWithoutFetching() > 0) {
	            Row r = (Row)rs.one();
	            bookingsforredis.add(setbooking(r, len));
	          } 
	          if (bookingsforredis.size() > 0) {
	            rc.setbookingsfornext4page(bookingsforredis, i);
	            bookingsforredis.clear();
	          } 
	        } 
	        endTime = System.currentTimeMillis();
	        System.out.println("for loop took " + (endTime - startTime) + " milliseconds");
	    	System.out.println("booking done + size: " + bookings.size());
	    	if(bookings.size()<pagesize)
	    		pagestate="null";
	        rc.setinredis(FlightResources.pagenum, pagestate);
	        pagestate = "";
	        endTime = System.currentTimeMillis();
	        for(BookingModel booking : bookings)
	        {
	        	System.out.println(booking.getId());
	        }
	        System.out.println(bookings.size());
	        return bookings;
	      } catch (Exception e) {
	        System.out.println("exception in getbookings from page" + e);
	      } 
	    } 
	    
	    else if (pageindex > FlightResources.pagenum && pagestatus.equals("nextpage")) {
	      try {
	        pagestate = rc.getfromradis(FlightResources.pagenum);
	        pagingstate = PagingState.fromString(pagestate);
	        for (int i = 1; i <= 4; i++) {
	          FlightResources.pagenum++;
	          System.out.println(FlightResources.pagenum);
	          if (FlightResources.username.equals("admin")) {
	            if (page.equals("past")) {
	              query = "select * from sanjai.pastbookflight";
	            } else {
	              query = "select * from sanjai.presentbookflight";
	            } 
	            Statement statement = new SimpleStatement(query).setFetchSize(pagesize).setPagingState(pagingstate);
	            rs = Cassandrautil.getsession().execute((Statement)statement);
	          } else {
	            if (page.equals("past")) {
	              query = "select * from sanjai.pastbookflight where name like ? allow filtering";
	            } else {
	              query = "select * from sanjai.presentbookflight where name like ? allow filtering";
	            } 
	            PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
	            BoundStatement statementBuilder = prepared.bind();
	            statementBuilder.bind(FlightResources.username).setPagingState(pagingstate).setFetchSize(pagesize);
	            rs = Cassandrautil.getsession().execute((Statement)statementBuilder);
	          } 
	          pagingstate = rs.getExecutionInfo().getPagingState();
	          pagestate = pagingstate.toString();
	          while (rs.getAvailableWithoutFetching() > 0) {
	            Row r = (Row)rs.one();
	            bookingsforredis.add(setbooking(r, len));
	          }
	          if(bookings.size()<pagesize)
		    		pagestate="null";
	        	  rc.setinredis(FlightResources.pagenum, pagestate);
	          pagestate = "";
	          if (bookingsforredis.size() > 0) {
	            System.out.println("he");
	            rc.setbookingsfornext4page(bookingsforredis, i);
	            bookingsforredis.clear();
	          } 
	        } 
	        return rc.getbookingsforpage(pageindex % 4, pagesize);
	      } catch (Exception e) {
	        System.out.println("exception in getbookings from page" + e);
	      } 
	    } else if (pagestatus.equals("previouspage") && pageindex <= FlightResources.pagenum - 4) {
	      System.out.println("hello");
	      try {
	        FlightResources.pagenum -= FlightResources.pagenum % 4 + 4;
	        pagestate = rc.getfromradis(FlightResources.pagenum);
	        pagingstate = PagingState.fromString(pagestate);
	        for (int i = 1; i <= 4; i++) {
	          bookingsforredis.clear();
	          FlightResources.pagenum++;
	          if (FlightResources.username.equals("admin")) {
	            if (page.equals("past")) {
	              query = "select * from sanjai.pastbookflight";
	            } else {
	              query = "select * from sanjai.presentbookflight";
	            } 
	            Statement statement = new SimpleStatement(query).setFetchSize(pagesize).setPagingState(pagingstate);
	            rs = Cassandrautil.getsession().execute((Statement)statement);
	          } else {
	            if (page.equals("past")) {
	              query = "select * from sanjai.pastbookflight where name like ? allow filtering";
	            } else {
	              query = "select * from sanjai.presentbookflight where name like ? allow filtering";
	            } 
	            PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
	            BoundStatement statementBuilder = prepared.bind();
	            statementBuilder.bind(FlightResources.username).setPagingState(pagingstate).setFetchSize(pagesize);
	            rs = Cassandrautil.getsession().execute(statementBuilder);
	          } 
	          pagingstate = rs.getExecutionInfo().getPagingState();
	          pagestate = pagingstate.toString();
	          while (rs.getAvailableWithoutFetching() > 0) {
	            Row r = (Row)rs.one();
	            bookingsforredis.add(setbooking(r, len));
	          } 	
	          if(bookings.size()<pagesize)
		    		pagestate="null";
	        	  rc.setinredis(FlightResources.pagenum, pagestate);
	          pagestate = "";
	          if (bookingsforredis.size() > 0)
	            rc.setbookingsfornext4page(bookingsforredis, i); 
	          bookingsforredis.clear();
	        } 
	        return rc.getbookingsforpage(pageindex % 4, pagesize);
	      } catch (Exception e) {
	        System.out.println("exception in getbookings from page" + e);
	      } 
	    } 
	    return null;
  }
  
  public int lengthofbookings(String page) {
    long stime = System.currentTimeMillis();
    long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    try {
      ResultSet result;
      long startTime = System.currentTimeMillis();
      long beforeusedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
      String cqlStatement = "";
      if (FlightResources.username.equals("admin")) {
        if (page.equals("past")) {
          cqlStatement = "SELECT count(*) FROM sanjai.pastbookflight ";
        } else {
          cqlStatement = "SELECT count(*) FROM sanjai.presentbookflight ";
        } 
        result = Cassandrautil.getsession().execute(cqlStatement);
      } else {
        String q = "select count(*) from sanjai.pastbookflight where name like ? allow filtering;";
        PreparedStatement prepare = Cassandrautil.getsession().prepare(q);
        BoundStatement b = prepare.bind(new Object[] { FlightResources.username });
        result = Cassandrautil.getsession().execute((Statement)b);
      } 
      Row r = (Row)result.one();
      Long i = Long.valueOf(r.getLong("count"));
      int j = i.intValue();
      Long endTime = Long.valueOf(System.currentTimeMillis());
      System.out.println("lengthofbookingsdb before return took time of " + (endTime.longValue() - startTime) + " milliseconds");
      long afterusedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
      long actualMemused = afterusedMem - beforeusedMem;
      System.out.println("lengthofbookingsdb before return took memory of" + (actualMemused / 1000L) + "KB");
      System.gc();
      return j;
    } catch (Exception e) {
      System.out.println("lengthofpastbooking" + e);
    } finally {
      long endTime = System.currentTimeMillis();
      long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
      long actualMemUsed = afterUsedMem - beforeUsedMem;
      System.out.println("lengthofbookingsdb after return took memory of " + (actualMemUsed / 1000L) + "KB");
      System.out.println("lengthofbookingsdb  after return took time of " + (endTime - stime) + " milliseconds");
    } 
    return 0;
  }
  
  public List<BookingModel> allsearch(String searchterm, String db, String searchclass, String searchvalid, String searchmeal) {
    BookingModel b = new BookingModel();
    List<BookingModel> bookings = new ArrayList<>();
    bookings.clear();
    List<BookingModel> temp = new ArrayList<>();
    if (searchclass.equals("null"))
      bookings.addAll(searchclass(searchterm, db, b)); 
    if (searchmeal.equals("null")) {
      temp = searchmealpref(searchterm, db, b);
      bookings.removeAll(temp);
      bookings.addAll(temp);
      System.out.println("endingmeals");
    } 
    if (searchvalid.equals("null")) {
      System.out.println("entering valid");
      temp = searchvalid(searchterm, db, b);
      bookings.removeAll(temp);
      bookings.addAll(temp);
    } 
    if (FlightResources.username.equals("admin")) {
      temp = searchname(searchterm, db, b);
      bookings.removeAll(temp);
      bookings.addAll(temp);
    } 
    return bookings;
  }
  
  private List<BookingModel> searchvalid(String searchterm, String db, BookingModel b) {
    long startTime = System.currentTimeMillis();
    List<BookingModel> bookings = new ArrayList<>();
    try {
      BoundStatement bound;
      String query = "";
      String search = "%" + searchterm + "%";
      if (FlightResources.username.equals("admin")) {
        if (db.equals("past")) {
          query = "Select * from sanjai.pastbookflight where validationstatus like ?";
        } else {
          query = "Select * from sanjai.presentbookflight where validationstatus like ?";
        } 
        PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
        bound = prepared.bind(new Object[] { search });
      } else {
        if (db.equals("past")) {
          query = "Select * from sanjai.pastbookflight where validationstatus like ? and name like ? allow filtering";
        } else {
          query = "Select * from sanjai.presentbookflight where validationstatus like ? and name like ? allow filtering";
        } 
        PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
        bound = prepared.bind(new Object[] { search, FlightResources.username });
      } 
      ResultSet result = Cassandrautil.getsession().execute((Statement)bound);
      List<Row> a = result.all();
      for (Row r : a)
        bookings.add(setbooking(r, 0)); 
    } catch (Exception e) {
      System.out.println("searchvalid" + e);
    } 
    long endTime = System.currentTimeMillis();
    System.out.println("validsearch took " + (endTime - startTime) + " milliseconds");
    return bookings;
  }
  
  private List<BookingModel> searchclass(String searchterm, String db, BookingModel b) {
    long startTime = System.currentTimeMillis();
    List<BookingModel> bookings = new ArrayList<>();
    try {
      BoundStatement bound;
      String query = "";
      String search = "%" + searchterm + "%";
      if (FlightResources.username.equals("admin")) {
        if (db.equals("past")) {
          query = "Select * from sanjai.pastbookflight where class like ?";
        } else {
          query = "Select * from sanjai.presentbookflight where class like ?";
        } 
        PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
        bound = prepared.bind(new Object[] { search });
      } else {
        if (db.equals("past")) {
          query = "Select * from sanjai.pastbookflight where class like ? and name like ? allow filtering";
        } else {
          query = "Select * from sanjai.presentbookflight where class like ? and name like ? allow filtering";
        } 
        PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
        bound = prepared.bind(new Object[] { search, FlightResources.username });
      } 
      ResultSet result = Cassandrautil.getsession().execute((Statement)bound);
      List<Row> a = result.all();
      for (Row r : a)
        bookings.add(setbooking(r, 0)); 
    } catch (Exception e) {
      System.out.println("searchclass" + e);
    } 
    long endTime = System.currentTimeMillis();
    System.out.println("class search took " + (endTime - startTime) + " milliseconds");
    return bookings;
  }
  
  private List<BookingModel> searchmealpref(String searchterm, String db, BookingModel b) {
    long startTime = System.currentTimeMillis();
    List<BookingModel> bookings = new ArrayList<>();
    try {
      BoundStatement bound;
      String search = "%" + searchterm + "%";
      String query = "";
      if (FlightResources.username.equals("admin")) {
        if (db.equals("past")) {
          query = "Select * from sanjai.pastbookflight where mealpref like ?";
        } else {
          query = "Select * from sanjai.presentbookflight where mealpref like ?";
        } 
        PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
        bound = prepared.bind(new Object[] { search });
      } else {
        if (db.equals("past")) {
          query = "Select * from sanjai.pastbookflight where mealpref like ? and name like ? allow filtering";
        } else {
          query = "Select * from sanjai.presentbookflight where mealpref like ? and name like ? allow filtering";
        } 
        PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
        bound = prepared.bind(new Object[] { search, FlightResources.username });
      } 
      ResultSet result = Cassandrautil.getsession().execute((Statement)bound);
      List<Row> a = result.all();
      for (Row r : a)
        bookings.add(setbooking(r, 0)); 
    } catch (Exception e) {
      System.out.println("searchmealpref" + e);
    } 
    long endTime = System.currentTimeMillis();
    System.out.println("mealssearch took " + (endTime - startTime) + " milliseconds");
    return bookings;
  }
  
  private List<BookingModel> searchname(String searchterm, String db, BookingModel b) {
    long start = System.currentTimeMillis();
    List<BookingModel> bookings = new ArrayList<>();
    try {
      BoundStatement bound;
      String search = "%" + searchterm + "%";
      String query = "";
      if (FlightResources.username.equals("admin")) {
        if (db.equals("past")) {
          query = "Select * from sanjai.pastbookflight where name like ?";
        } else {
          query = "Select * from sanjai.presentbookflight where name like ?";
        } 
        long l1 = System.currentTimeMillis();
        PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
        bound = prepared.bind(new Object[] { search });
        long l2 = System.currentTimeMillis();
        System.out.println("name search prepare statement took " + (l2 - l1) + " milliseconds");
      } else {
        if (db.equals("past")) {
          query = "Select * from sanjai.pastbookflight where name like ?";
        } else {
          query = "Select * from sanjai.presentbookflight where name like ?";
        } 
        PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
        bound = prepared.bind(new Object[] { search });
      } 
      long startTime = System.currentTimeMillis();
      ResultSet result = Cassandrautil.getsession().execute((Statement)bound);
      long endTime = System.currentTimeMillis();
      System.out.println("name search resultset took " + (endTime - startTime) + " milliseconds");
      startTime = System.currentTimeMillis();
      List<Row> a = result.all();
      endTime = System.currentTimeMillis();
      System.out.println("name search list took " + (endTime - startTime) + " milliseconds");
      startTime = System.currentTimeMillis();
      for (Row r : a)
        bookings.add(setbooking(r, 0)); 
      endTime = System.currentTimeMillis();
    } catch (Exception e) {
      System.out.println("searchname" + e);
    } 
    long end = System.currentTimeMillis();
    System.out.println("name search for took " + (end - start) + " milliseconds");
    return bookings;
  }
  
  public List<BookingModel> getallbookings(String s) {
    List<Row> a = new ArrayList<>();
    List<BookingModel> booking = new ArrayList<>();
    try {
      String query = "";
      if (s.equals("past")) {
        query = "SELECT * FROM sanjai.pastbookflight";
      } else {
        query = "SELECT * FROM sanjai.presentbookflight";
      } 
      ResultSet result = Cassandrautil.getsession().execute(query);
      a = result.all();
      for (Row r : a)
        booking.add(setbooking(r, 0)); 
    } catch (Exception e) {
      System.out.println("returninvalidbooking" + e);
    } 
    System.out.println("executing");
    return booking;
  }
  
  public List<BookingModel> categorysearch(String tablename, String searchclass, String searchvalid, String searchmeal) {
    List<BookingModel> bookings = new ArrayList<>();
    try {
      BoundStatement bound;
      System.out.println("nside try");
      String query = "";
      if (FlightResources.username.equals("admin")) {
        if (searchclass.equals("null") && searchvalid.equals("null")) {
          query = "Select * from sanjai." + tablename + " where mealpref like ? allow filtering";
          PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
          bound = prepared.bind(new Object[] { searchmeal });
        } else if (searchvalid.equals("null") && searchmeal.equals("null")) {
          query = "Select * from sanjai." + tablename + " where class like ? allow filtering";
          PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
          bound = prepared.bind(new Object[] { searchclass });
        } else if (searchclass.equals("null") && searchmeal.equals("null")) {
          System.out.println("hi");
          query = "Select * from sanjai." + tablename + " where validationstatus like ? allow filtering";
          PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
          bound = prepared.bind(new Object[] { searchvalid });
        } else if (searchclass.equals("null")) {
          query = "Select * from sanjai." + tablename + " where validationstatus like ? and mealpref like ? allow filtering";
          PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
          bound = prepared.bind(new Object[] { searchvalid, searchmeal });
        } else if (searchvalid.equals("null")) {
          query = "Select * from sanjai." + tablename + " where mealpref like ? and class like ? allow filtering";
          PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
          bound = prepared.bind(new Object[] { searchmeal, searchclass });
        } else if (searchmeal.equals("null")) {
          query = "Select * from sanjai." + tablename + " where validationstatus like ? and class like ? allow filtering";
          PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
          bound = prepared.bind(new Object[] { searchvalid, searchclass });
        } else {
          query = "Select * from sanjai." + tablename + " where validationstatus like ? and mealpref like ? and class like ? allow filtering";
          PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
          bound = prepared.bind(new Object[] { searchvalid, searchmeal, searchclass });
        } 
      } else if (searchclass.equals("null") && searchvalid.equals("null")) {
        query = "Select * from sanjai." + tablename + " where mealpref like ? and name like ? allow filtering";
        PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
        bound = prepared.bind(new Object[] { searchmeal, FlightResources.username });
      } else if (searchvalid.equals("null") && searchmeal.equals("null")) {
        System.out.println("inside class filter");
        query = "Select * from sanjai." + tablename + " where class like ? and name like ? allow filtering";
        PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
        bound = prepared.bind(new Object[] { searchclass, FlightResources.username });
      } else if (searchclass.equals("null") && searchmeal.equals("null")) {
        System.out.println("inside valid filter");
        query = "Select * from sanjai." + tablename + " where validationstatus like ? and name like ? allow filtering";
        PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
        System.out.println(searchvalid);
        System.out.println(FlightResources.username);
        bound = prepared.bind(new Object[] { searchvalid, FlightResources.username });
      } else if (searchclass.equals("null")) {
        query = "Select * from sanjai." + tablename + " where validationstatus like ? and mealpref like ? and name like ? allow filtering";
        PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
        bound = prepared.bind(new Object[] { searchvalid, searchmeal, FlightResources.username });
      } else if (searchvalid.equals("null")) {
        query = "Select * from sanjai." + tablename + " where mealpref like ? and class like ? and name like ? allow filtering";
        PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
        bound = prepared.bind(new Object[] { searchmeal, searchclass, FlightResources.username });
      } else if (searchmeal.equals("null")) {
        query = "Select * from sanjai." + tablename + " where validationstatus like ? and class like ? and name like ? allow filtering";
        PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
        bound = prepared.bind(new Object[] { searchvalid, searchclass, FlightResources.username });
      } else {
        query = "Select * from sanjai." + tablename + " where validationstatus like ? and mealpref like ? and class like ? and name like ? allow filtering";
        PreparedStatement prepared = Cassandrautil.getsession().prepare(query);
        bound = prepared.bind(new Object[] { searchvalid, searchmeal, searchclass, FlightResources.username });
      } 
      ResultSet result = Cassandrautil.getsession().execute((Statement)bound);
      List<Row> a = result.all();
      for (Row r : a)
        bookings.add(setbooking(r, 0)); 
    } catch (Exception e) {
      System.out.println("returninvalidbooking" + e);
    } 
    return bookings;
  }


}
