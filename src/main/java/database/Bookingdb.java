package database;

import com.sanjai.airplane.FlightResources;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.BookingModel;
import model.Bookingclass;
import model.FlightModel;

public class Bookingdb {
  Bookingcassandradb dbc = new Bookingcassandradb();
  
  public List<BookingModel> returnpresentbooking() {
    List<BookingModel> bookings = new ArrayList<>();
    try { 
      ResultSet rs;
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/sanjai", "root", "321san123");
      String query = "";
      System.out.println("bfore" + FlightResources.username); 
      if (FlightResources.username.equals("admin")) {
        query = "select * from bookflight where dateofbooking>=curdate()&&validationstatus='valid' order by dateofbooking desc;";
        Statement stmt = con.createStatement();
        rs = stmt.executeQuery(query);
        System.out.println("currentuseradmin:" + FlightResources.username);
      } else {
        query = "select * from bookflight where name=? && dateofbooking>=curdate() && validationstatus='valid' order by dateofbooking desc;";
        PreparedStatement pstmnt = con.prepareStatement(query);
        pstmnt.setString(1, FlightResources.username);
        rs = pstmnt.executeQuery();
        System.out.println("currentuser:" + FlightResources.username);
      } 
      while (rs.next()) {
        BookingModel b = new BookingModel();
        b.setId(rs.getString(1));
        b.setUserid(rs.getInt(2));
        b.setName(rs.getString(3));
        b.setMeals(rs.getString(4));
        b.setTickets(rs.getInt(5));
        b.setTravelclass(rs.getString(6));
        b.setFlight(rs.getInt(7));
        b.setAmount(rs.getInt(8));
        b.setDateofbooking(rs.getString(9));
        b.setValidationstatus(rs.getString(10));
        bookings.add(b);
      } 
    } catch (Exception ex) {
      System.out.println(ex);
    } 
    return bookings;
  }
  
  public List<BookingModel> returninvalidbooking() {
    List<BookingModel> bookings = new ArrayList<>();
    try {
      ResultSet rs;
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/sanjai", "root", "321san123");
      String query = "";
      System.out.println("bfore" + FlightResources.username);
      if (FlightResources.username.equals("admin")) {
        query = "select * from bookflight where validationstatus='invalid' order by dateofbooking desc;";
        Statement stmt = con.createStatement();
        rs = stmt.executeQuery(query);
        System.out.println("currentuseradmin:" + FlightResources.username);
      } else {
        query = "select * from bookflight where name=? && validationstatus='invalid' order by dateofbooking desc;";
        PreparedStatement pstmnt = con.prepareStatement(query);
        pstmnt.setString(1, FlightResources.username);
        rs = pstmnt.executeQuery();
        System.out.println("currentuser:" + FlightResources.username);
      } 
      while (rs.next()) {
        BookingModel b = new BookingModel();
        b.setId(rs.getString(1));
        b.setUserid(rs.getInt(2));
        b.setName(rs.getString(3));
        b.setMeals(rs.getString(4));
        b.setTickets(rs.getInt(5));
        b.setTravelclass(rs.getString(6));
        b.setFlight(rs.getInt(7));
        b.setAmount(rs.getInt(8));
        b.setDateofbooking(rs.getString(9));
        b.setValidationstatus(rs.getString(10));
        bookings.add(b);
      } 
    } catch (Exception ex) {
      System.out.println(ex);
    } 
    return bookings;
  }
  
  public List<BookingModel> returninvalidsearchbooking(int flightnum) {
    List<BookingModel> bookings = new ArrayList<>();
    try {
      ResultSet rs;
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/sanjai", "root", "321san123");
      String query = "";
      System.out.println("bfore" + FlightResources.username);
      if (FlightResources.username.equals("admin")) {
        query = "select * from bookflight where validationstatus='invalid' && flightnum=? order by dateofbooking desc;";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setInt(1, flightnum);
        rs = pstmt.executeQuery();
        System.out.println("currentuseradmin:" + FlightResources.username);
      } else {
        query = "select * from bookflight where name=? && validationstatus='invalid' && flightnum=? order by dateofbooking desc;";
        PreparedStatement pstmnt = con.prepareStatement(query);
        pstmnt.setString(1, FlightResources.username);
        pstmnt.setInt(2, flightnum);
        rs = pstmnt.executeQuery();
        System.out.println("currentuser:" + FlightResources.username);
      } 
      while (rs.next()) {
        BookingModel b = new BookingModel();
        b.setId(rs.getString(1));
        b.setUserid(rs.getInt(2));
        b.setName(rs.getString(3));
        b.setMeals(rs.getString(4));
        b.setTickets(rs.getInt(5));
        b.setTravelclass(rs.getString(6));
        b.setFlight(rs.getInt(7));
        b.setAmount(rs.getInt(8));
        b.setDateofbooking(rs.getString(9));
        b.setValidationstatus(rs.getString(10));
        bookings.add(b);
      } 
    } catch (Exception ex) {
      System.out.println(ex);
    } 
    return bookings;
  }
  
  public void refreshpastBooking() {
    System.out.println("refreshing");
    List<BookingModel> bookings = new ArrayList<>();
    try {
      ResultSet rs;
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/sanjai", "root", "321san123");
      String query = "";
      System.out.println("username-" + FlightResources.username);
      if (FlightResources.username.equals("admin")) {
        query = "select * from bookflight where dateofbooking<curdate()";
        Statement stmt = con.createStatement();
        rs = stmt.executeQuery(query);
      } else {
        query = "select * from bookflight where name=? && dateofbooking<curdate()";
        PreparedStatement pstmnt = con.prepareStatement(query);
        pstmnt.setString(1, FlightResources.username);
        rs = pstmnt.executeQuery();
      } 
      while (rs.next()) {
        BookingModel b = new BookingModel();
        b.setId(rs.getString(1));
        
        b.setUserid(rs.getInt(2));
        b.setName(rs.getString(3));
        b.setMeals(rs.getString(4));
        b.setTickets(rs.getInt(5));
        b.setTravelclass(rs.getString(6));
        b.setFlight(rs.getInt(7));
        b.setAmount(rs.getInt(8));
        b.setDateofbooking(rs.getString(9));
        b.setValidationstatus(rs.getString(10));
        bookings.add(b);
      } 
      this.dbc.insertpastbookingstocassandra(bookings);
      deletepastbookings();
    } catch (Exception ex) {
      System.out.println(ex);
    } 
  }
  
  public List<FlightModel> allflights() {
    List<FlightModel> flights = new ArrayList<>();
    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/sanjai", "root", "321san123");
      String query = "select * from flights";
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        FlightModel f = new FlightModel();
        f.setId(rs.getString(1));
        f.setFlightnumber(rs.getInt(2));
        f.setBusinessclass(rs.getInt(3));
        f.setEconomyclass(rs.getInt(4));
        f.setSource(rs.getString(5));
        f.setDestination(rs.getString(6));
        f.setDaysoftravel(rs.getString(7));
        flights.add(f);
      } 
    } catch (Exception ex) {
      System.out.println(ex);
    } 
    return flights;
  }
  
  public void create(BookingModel b) {
    try {
      int x = getmaxid();
      int currentuserid = getuserid(FlightResources.username);
      System.out.println("current:" + currentuserid);
      System.out.println("maxid" + x);
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/sanjai", "root", "321san123");
      String query = "insert into bookflight(userid,name,mealpref,tickets,class,flightnum,amount,dateofbooking,validationstatus) values(?,?,?,?,?,?,?,STR_TO_DATE(?,'%Y-%m-%d'),?);";
      PreparedStatement pstmt = con.prepareStatement(query);
      pstmt.setInt(1, currentuserid);
      pstmt.setString(2, b.getName());
      pstmt.setString(3, b.getMeals());
      pstmt.setInt(4, b.getTickets());
      pstmt.setString(5, b.getTravelclass());
      pstmt.setInt(6, b.getFlight());
      pstmt.setInt(7, b.getAmount());
      pstmt.setString(8, b.getDateofbooking());
      pstmt.setString(9, b.getValidationstatus());
      pstmt.executeUpdate();
    } catch (Exception ex) {
      System.out.println("error" + ex);
    } 
  }
  
  public void createroundtrip(List<BookingModel> rb) {
    try {
      int x = getmaxid();
      int currentuserid = getuserid(FlightResources.username);
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/sanjai", "root", "321san123");
      for (BookingModel roundbook : rb) {
        x++;
        String query = "insert into bookflight(userid,name,mealpref,tickets,class,flightnum,amount,dateofbooking,validationstatus) values(?,?,?,?,?,?,?,STR_TO_DATE(?,'%Y-%m-%d'),?);";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setInt(1, currentuserid);
        pstmt.setString(2, roundbook.getName());
        pstmt.setString(3, roundbook.getMeals());
        pstmt.setInt(4, roundbook.getTickets());
        pstmt.setString(5, roundbook.getTravelclass());
        pstmt.setInt(6, roundbook.getFlight());
        pstmt.setInt(7, roundbook.getAmount());
        pstmt.setString(8, roundbook.getDateofbooking());
        pstmt.setString(9, roundbook.getValidationstatus());
        pstmt.executeUpdate();
      } 
    } catch (Exception e) {
      System.out.println(e);
    } 
  }
  
  public void createflight(FlightModel f) {
    try {
      int x = getmaxflightid();
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/sanjai", "root", "321san123");
      String query = "insert into flights values (?,?,?,?,?,?,?)";
      PreparedStatement pstmt = con.prepareStatement(query);
      pstmt.setInt(1, x + 1);
      pstmt.setInt(2, f.getFlightnumber());
      pstmt.setInt(3, f.getBusinessclass());
      pstmt.setInt(4, f.getEconomyclass());
      pstmt.setString(5, f.getSource());
      pstmt.setString(6, f.getDestination());
      pstmt.setString(7, f.getDaysoftravel());
      pstmt.executeUpdate();
    } catch (Exception ex) {
      System.out.print(ex);
    } 
  }
  
  public void editflight(FlightModel f) {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/sanjai", "root", "321san123");
      checkvalidbooking(f);
      String query = "update flights set businessclass=?,economyclass=?,source=?,destination=?,daysoftravel=? where number=?;";
      PreparedStatement pstmt = con.prepareStatement(query);
      pstmt.setInt(1, f.getBusinessclass());
      pstmt.setInt(2, f.getEconomyclass());
      pstmt.setString(3, f.getSource());
      pstmt.setString(4, f.getDestination());
      pstmt.setString(5, f.getDaysoftravel());
      pstmt.setInt(6, f.getFlightnumber());
      pstmt.executeUpdate();
    } catch (Exception e) {
      System.out.println("edit" + e);
    } 
  }
  
  public BookingModel getBooking(int index) {
    BookingModel booking = new BookingModel();
    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/sanjai", "root", "321san123");
      String query = "select * from bookflight where id=?";
      PreparedStatement pstmt = con.prepareStatement(query);
      pstmt.setInt(1, index);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        booking.setId(rs.getString(1));
        booking.setUserid(rs.getInt(2));
        booking.setName(rs.getString(3));
        booking.setMeals(rs.getString(4));
        booking.setTickets(rs.getInt(5));
        booking.setTravelclass(rs.getString(6));
        booking.setFlight(rs.getInt(7));
        booking.setAmount(rs.getInt(8));
        booking.setDateofbooking(rs.getString(9));
        booking.setValidationstatus(rs.getString(10));
      } 
    } catch (Exception ex) {
      System.out.println(ex);
    } 
    return booking;
  }
  
  public FlightModel getflight(String index) {
    FlightModel flight = new FlightModel();
    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/sanjai", "root", "321san123");
      String query = "select * from flights where id=?";
      PreparedStatement pstmt = con.prepareStatement(query);
      pstmt.setString(1, index);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        flight.setId(rs.getString(1));
        flight.setFlightnumber(rs.getInt(2));
        flight.setBusinessclass(rs.getInt(3));
        flight.setEconomyclass(rs.getInt(4));
        flight.setSource(rs.getString(5));
        flight.setDestination(rs.getString(6));
        flight.setDaysoftravel(rs.getString(7));
      } 
    } catch (Exception ex) {
      System.out.println(ex);
    } 
    return flight;
  }
  
  public void delete(int index) {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/sanjai", "root", "321san123");
      String query = "delete from bookflight where id=?";
      PreparedStatement pstmt = con.prepareStatement(query);
      pstmt.setInt(1, index);
      pstmt.executeUpdate();
    } catch (Exception ex) {
      System.out.print(ex);
    } 
  }
  
  public int getmaxid() {
    int max = 0;
    try {
      System.out.println("getmaxid");
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/sanjai", "root", "321san123");
      String query = "select id from bookflight order by id desc limit 1";
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(query);
      if (rs.next())
        max = rs.getInt(1); 
    } catch (Exception ex) {
      System.out.print(ex);
    } 
    return max;
  }
  
  public int getmaxflightid() {
    int max = 0;
    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/sanjai", "root", "321san123");
      String query = "select id from flights order by id desc limit 1";
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(query);
      if (rs.next())
        max = rs.getInt(1); 
    } catch (Exception ex) {
      System.out.print(ex);
    } 
    return max;
  }
  
  public int getuserid(String uname) {
    int uid = 0;
    try {
      System.out.println("no err");
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/sanjai", "root", "321san123");
      String query = "select id from users where username=?";
      PreparedStatement pstmt = con.prepareStatement(query);
      pstmt.setString(1, uname);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next())
        uid = rs.getInt(1); 
    } catch (Exception ex) {
      System.out.println("error at get user");
      System.out.println(ex);
    } 
    System.out.println("funct:" + uid);
    return uid;
  }
  
  public void deleteflight(int index) {
    int flightnum = 0;
    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/sanjai", "root", "321san123");
      String q = "select number from flights where id=?";
      PreparedStatement pstmt1 = con.prepareStatement(q);
      pstmt1.setInt(1, index);
      ResultSet rs = pstmt1.executeQuery();
      if (rs.next())
        flightnum = rs.getInt(1); 
      String query = "delete from flights where id=?";
      PreparedStatement pstmt = con.prepareStatement(query);
      pstmt.setInt(1, index);
      System.out.println(flightnum);
      pstmt.executeUpdate();
      checkvalidation(flightnum);
    } catch (Exception ex) {
      System.out.print(ex);
    } 
  }
  
  public void checkvalidation(int flightnum) {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/sanjai", "root", "321san123");
      String query = "update bookflight set validationstatus=\"invalid\" where flightnum=?;";
      PreparedStatement pstmt = con.prepareStatement(query);
      pstmt.setInt(1, flightnum);
      pstmt.executeUpdate();
    } catch (Exception e) {
      System.out.println(e);
    } 
  }
  
  public void updateavailableseats(int numoftickets, String classofbooking, int flightnum) {
    int tempseats = 0;
    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/sanjai", "root", "321san123");
      if (classofbooking.equals("business")) {
        String query = "select availablebusiness from flights where number=?";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setInt(1, flightnum);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next())
          tempseats = rs.getInt(1); 
        tempseats -= numoftickets;
        String query1 = "update bookflight set availablebusiness=? where number=?";
        PreparedStatement pstmt1 = con.prepareStatement(query1);
        pstmt1.setInt(1, flightnum);
        pstmt1.executeUpdate();
      } else {
        String query = "select availableeconomy from flights where number=?";
        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setInt(1, flightnum);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next())
          tempseats = rs.getInt(1); 
        tempseats -= numoftickets;
        String query1 = "update bookflight set availableeconomy=? where number=?";
        PreparedStatement pstmt1 = con.prepareStatement(query1);
        pstmt1.setInt(1, flightnum);
        pstmt1.executeUpdate();
      } 
    } catch (Exception e) {
      System.out.println(e);
    } 
  }
  
  public void deletepastbookings() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/sanjai", "root", "321san123");
      String q = "delete from bookflight where dateofbooking<curdate()";
      Statement stmt = con.createStatement();
      stmt.executeUpdate(q);
    } catch (Exception ex) {
      System.out.print(ex);
    } 
  }
  
  public void checkvalidbooking(FlightModel f) {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/sanjai", "root", "321san123");
      String query = "select * from flights where number=?";
      PreparedStatement pstmt1 = con.prepareStatement(query);
      pstmt1.setInt(1, f.getFlightnumber());
      ResultSet rs = pstmt1.executeQuery();
      FlightModel oldflight = new FlightModel();
      if (rs.next()) {
        oldflight.setId(rs.getString(1));
        oldflight.setFlightnumber(rs.getInt(2));
        oldflight.setBusinessclass(rs.getInt(3));
        oldflight.setEconomyclass(rs.getInt(4));
        oldflight.setSource(rs.getString(5));
        oldflight.setDestination(rs.getString(6));
        oldflight.setDaysoftravel(rs.getString(7));
        if (oldflight.getDestination() != f.getDestination() || oldflight.getSource() != oldflight.getSource())
          setinvalidbooking(f.getFlightnumber()); 
        if (oldflight.getDaysoftravel() == "alldays" && f.getDaysoftravel() == "weekends")
          checkandsetinvalidbooking(f.getFlightnumber(), "weekends"); 
        if (oldflight.getDaysoftravel() == "alldays" && f.getDaysoftravel() == "weekdays")
          checkandsetinvalidbooking(f.getFlightnumber(), "weekdays"); 
        if (oldflight.getDaysoftravel() == "weekdays" && f.getDaysoftravel() == "weekends")
          setinvalidbooking(f.getFlightnumber()); 
        if (oldflight.getDaysoftravel() == "weekends" && f.getDaysoftravel() == "weekdays")
          setinvalidbooking(f.getFlightnumber()); 
      } 
    } catch (Exception e) {
      System.out.println("checkvalid" + e);
    } 
  }
  
  private void setinvalidbooking(int flightnum) {
    String query = "update bookflight set validationstatus='invalid' where flightnum=?";
    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/sanjai", "root", "321san123");
      PreparedStatement pstmt1 = con.prepareStatement(query);
      pstmt1.setInt(1, flightnum);
      pstmt1.executeUpdate();
    } catch (Exception e) {
      System.out.println("setinvalid" + e);
    } 
  }
  
  public void checkandsetinvalidbooking(int flightnum, String dayoftravel) {
    Bookingclass bc = new Bookingclass();
    String query = "";
    try {
      query = "select id,dateofbooking from bookflights where flightnum=?";
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/sanjai", "root", "321san123");
      PreparedStatement pstmt1 = con.prepareStatement(query);
      pstmt1.setInt(1, flightnum);
      ResultSet rs = pstmt1.executeQuery();
      while (rs.next()) {
        if (dayoftravel == "weekends"){
          if (bc.checkweekend(rs.getString(2))) {
            String query1 = "update bookflight set validationstatus='invalid' where id=?";
            PreparedStatement p = con.prepareStatement(query1);
            p.setInt(1, rs.getInt(1));
            p.executeUpdate();
          }  }
        if (dayoftravel == "weekdays"){
          if (!bc.checkweekend(rs.getString(2))) {
            String query1 = "update bookflight set validationstatus='invalid' where id=?";
            PreparedStatement p = con.prepareStatement(query1);
            p.setInt(1, rs.getInt(1));
            p.executeUpdate();
          }}  
      } 
    } catch (Exception e) {
      System.out.println("checkandset " + e);
    } 
  }
  
}