package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
 
import com.sanjai.airplane.FlightResources;

import model.UserModel;

public class Userdb {
  public void createuser(UserModel user) {
    try {
    	System.out.println("insidecreateuser");
      int x = getmaxid();
      System.out.println(x); 
      Class.forName("com.mysql.jdbc.Driver");
      System.out.println("afterclass");
//      Connection con = DriverManager.getConnection("jdbc:mysql://192.168.0.105:3306/sanjai?autoReconnect=true&useSSL=false", "root", "321san123");
      Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sanjai?autoReconnect=true&useSSL=false", "root", "");
      //Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/sanjai?autoReconnect=true&useSSL=false", "root", "321san123");
      String query = "insert into users values (?,?,?,?)";
      System.out.println("afterconn");
      PreparedStatement pstmt = con.prepareStatement(query);
      pstmt.setInt(1, x + 1);
      pstmt.setString(2, user.getUsername()); 
      pstmt.setString(3, user.getEmail());
      pstmt.setString(4, user.getPassword());
      System.out.println("before exec");
      pstmt.executeUpdate();
      System.out.println("after exec");
      System.out.println("inserted successfully "+user.getUsername());
    } catch (Exception ex) {
      System.out.print(ex);
    } 
  }
  
  public int getmaxid() {
    int max = 0;
    try {
    	System.out.println("insidegetmaxdb");
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sanjai?autoReconnect=true&useSSL=false", "root", "");
      //Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/sanjai?autoReconnect=true&useSSL=false", "root", "321san123");
      String query = "select id from users order by id desc limit 1";
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(query);
      if (rs.next())
        max = rs.getInt(1); 
    } catch (Exception ex) {
      System.out.print(ex);
    } 
    return max;
  }
  
  public boolean isadmindb(String username) {
    try {
    	System.out.println("insideisadmindb");
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sanjai?autoReconnect=true&useSSL=false", "root", "");
      //Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/sanjai?autoReconnect=true&useSSL=false", "root", "321san123");
      String query = "select username from users where username=?";
      PreparedStatement ptmnt = con.prepareStatement(query);
      ptmnt.setString(1, username);
      ResultSet rs = ptmnt.executeQuery();
      if (rs.next())
        if (rs.getString(1).equals("admin"))
          return true;  
    } catch (Exception e) {
      System.out.println("isadmin"+e);
    } 
    return false;
  }
  
  public String fetchuserbymail(String mail) {
    UserModel user = new UserModel();
    try {
    	System.out.println("insidefetch");
      Class.forName("com.mysql.jdbc.Driver");
      Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sanjai?autoReconnect=true&useSSL=false", "root", "");
      //Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/sanjai?autoReconnect=true&useSSL=false", "root", "321san123");
      String q = "select * from users where email=?";
      PreparedStatement pstmt1 = con.prepareStatement(q);
      pstmt1.setString(1, mail);
      ResultSet rs = pstmt1.executeQuery();
      if (rs.next()) {
        user.setId(rs.getInt(1));
        user.setUsername(rs.getString(2));
        user.setEmail(rs.getString(3));
        user.setPassword(rs.getString(4));
      } 
    } catch (Exception ex) {
      System.out.println("exception" + ex);
    } 
    System.out.println("create");
    return user.getEmail();
  }
  
  public UserModel checklogin(UserModel user) {
    UserModel user1 = new UserModel();
    try {
    	System.out.println("insidechecklogindb");
      Class.forName("com.mysql.jdbc.Driver");
      //Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/sanjai?autoReconnect=true&useSSL=false", "root", "321san123");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sanjai?autoReconnect=true&useSSL=false", "root", "");
//      Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/sanjai?autoReconnect=true&useSSL=false", "root", "321san123");
      String q = "select * from users where username=? && password=?";
      System.out.println(q);
      PreparedStatement pstmt1 = con.prepareStatement(q);
      pstmt1.setString(1, user.getUsername());
      pstmt1.setString(2, user.getPassword());
      ResultSet rs = pstmt1.executeQuery();
      if (rs.next()) { 
        user1.setId(rs.getInt(1));
        user1.setUsername(rs.getString(2));
        user1.setEmail(rs.getString(3));
        user1.setPassword(rs.getString(4));
      } 
      System.out.println(user1.getEmail());
    } catch (Exception ex) {
      System.out.println("exception" + ex);
    } 
    return user1;
  }
  
  public boolean checkloginvalid(String uname, String password) {
    try {
    	System.out.println("insidecheckloginvaliddb");
      Class.forName("com.mysql.jdbc.Driver");
     Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sanjai?autoReconnect=true&useSSL=false", "root", "");
 //     Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/sanjai?autoReconnect=true&useSSL=false", "root", "321san123");
      String q = "select * from users where username=? && password=?";
      PreparedStatement pstmt1 = con.prepareStatement(q);
      pstmt1.setString(1, uname);
      pstmt1.setString(2, password);
      ResultSet rs = pstmt1.executeQuery();
      System.out.println("checking");
      if (rs.next())
      {
    	  System.out.println("true");
    	  return true; 
      }
    } catch (Exception e) {
      System.out.print("Exception " + e);
    } 
    System.out.println("false");
    return false;
  }

public List<UserModel> getallusers() {
	// TODO Auto-generated method stub
	System.out.println("insidegetallusersdb");
	List<UserModel> users=new ArrayList<UserModel>();
	try {
	      Class.forName("com.mysql.jdbc.Driver");
//	      Connection con = DriverManager.getConnection("jdbc:mysql://192.168.0.105:3306/sanjai?autoReconnect=true&useSSL=false", "root", "321san123");
	      Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sanjai?autoReconnect=true&useSSL=false", "root", "");
//	      Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/sanjai?autoReconnect=true&useSSL=false", "root", "321san123");
	      String query = "select id,username,email from users";
	      Statement stmt = con.createStatement();
	      ResultSet rs = stmt.executeQuery(query);
	      while(rs.next())
	      {
	    	  UserModel user=new UserModel();
	    	  user.setId(rs.getInt("id"));
	    	  user.setUsername(rs.getString("username"));
	    	  user.setEmail(rs.getString("email"));
	    	  user.setPassword("");
	    	  users.add(user);
	      }
	      //System.out.println(users.size());
		for(UserModel user : users)
		{
			System.out.println(user.getUsername());
		}
		}
	 catch (Exception e) {
	      System.out.print("Exception " + e);
	    } 
	

	
	return users;
}
}
