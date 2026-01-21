package project.library;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
public class App 
{
	Connection c = null;
	Statement st = null;
	
	public String addNewBook (String id, String author,String book_name,String publication_year,String publisher,String publication_city,String location, int bookcase_number,String cost) throws SQLException {
		try {
    		Class.forName("org.postgresql.Driver");
    		c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library", "postgres", "postgres");
    		System.out.println( "Open Successfully!" );
    		st = c.createStatement(); 
    		String sql = "INSERT INTO books(id,author,book_name,publication_year,publisher,publication_city,location,cost) "
    		+ "VALUES(" + "'"+id + "'"+"," + "'"+author+"'"+","+ "'"+ book_name +"'"+ "," + "'"+publication_year + "'"+"," 
    				+"'"+ publisher + "'"+"," + "'"+publication_city + "'"+"," + "'"+location + "'"+"," + "'"+cost + "'"+ ")";
    		st.executeUpdate(sql);
    		st.close();
    		c.close();
    		try (Location book = new Location("bolt://localhost:7687", "neo4j", "neo4jn")) {
        		book.addBookLocation(bookcase_number, id, location);
        	}
    		return "Book Added";
    	} catch (Exception e) {
    		System.out.println("Connection Failed");
    		e.printStackTrace();
    		return "fail";
    	}
	}
	
	public boolean checkStudentForBooks (String id) {
		try {
    		Class.forName("org.postgresql.Driver");
    		c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library", "postgres", "postgres");
    		System.out.println( "Open Successfully!" );
    		st = c.createStatement();
    		String sql = "SELECT book_quantity FROM students WHERE student_id =" + "'" +id +"'";
    		ResultSet rs = st.executeQuery(sql);
    		String s = null;
    		while(rs.next()) {
    		
    			s = rs.getString("book_quantity"); 
    		}
    		rs.close();
    		st.close();
    		c.close();
    		if (s.equals("10")) {
    			return false;
    		} else {
    			return true;
    		}
    	} catch (Exception e) {
    		System.out.println("Connection Failed");
    		e.printStackTrace();
    		return false;
    	}
	}
	
	public ArrayList<String> booksWithIssueDate() {
		try {
    		Class.forName("org.postgresql.Driver");
    		c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library", "postgres", "postgres");
    		//System.out.println( "Open Successfully!" );
    		st = c.createStatement();
    		//System.out.println("   id      " + "       name        " + "   author   "+ "     date    ");
    		ResultSet rs = st.executeQuery("SELECT books.id, book_name, issue_date FROM books, orders WHERE books.id = orders.book_id AND back_date IS NULL");
    		ArrayList<String> id = new ArrayList<String>();String s = null;
    		while(rs.next()) {
    			s = String.format("| %-10s | %-70s | %-10s %n", rs.getString("id"),rs.getString("book_name"),rs.getString("issue_date"));
    			id.add(s);
    			
    		}
    		rs.close();
    		st.close();
    		c.close();
    		for(String si:id) {
    			System.out.printf(si);
    			
    		}
    		return id;
    	} catch (Exception e) {
    		System.out.println("Connection Failed");
    		e.printStackTrace();
    		return new ArrayList<String>();
    	}
	}
	
	public ArrayList<String> studentsLate () {
		try {
    		Class.forName("org.postgresql.Driver");
    		c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library", "postgres", "postgres");
    		//System.out.println( "Open Successfully!" );
    		st = c.createStatement();
    		ResultSet rs = st.executeQuery("SELECT orders.student_id, name_surname FROM orders,students WHERE issue_date + integer '15' <  CURRENT_DATE AND back_date IS NULL AND orders.student_id = students.student_id");
    		ArrayList<String> students_id = new ArrayList<String>(); String s = null;
    		//ArrayList<String> students_name = new ArrayList<String>();
    		while(rs.next()) {
    			s = String.format("| %-2s | %-20s %n", rs.getString("student_id"),rs.getString("name_surname"));
    			students_id.add(s);
    			//students_name.add(rs.getString("name_surname"));
    		}
    		for(String si:students_id) {
    			System.out.printf(si);
    			
    		}
    		rs.close();
    		st.close();
    		c.close();
    		return students_id;
    		//System.out.printf("| %-10s | %-8s | %04d |%n", "Integral", "byte",    8);
    	} catch (Exception e) {
    		System.out.println("Connection Failed");
    		e.printStackTrace();
    		return new ArrayList<String>();
    	}
	}
	
	public ArrayList<String> newCost(int year, String coef) {
		try {
    		Class.forName("org.postgresql.Driver");
    		c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library", "postgres", "postgres");
    		//System.out.println( "Open Successfully!" );
    		st = c.createStatement();
    		ResultSet rs = st.executeQuery("SELECT id, book_name, cost * float" +"'"+coef+"' "+ "AS new_price FROM books WHERE publication_year = " + year);
    		ArrayList<String> books = new ArrayList<String>(); String s = null;
    		while(rs.next()) {
    			s = String.format("| %-2s | %-100s | %-10s %n", rs.getString("id"),rs.getString("book_name"),rs.getString("new_price"));
    			books.add(s);
    		}
    		rs.close();
    		st.close();
    		c.close();
    		for(String si:books) {
    			System.out.printf(si);
    		}
    		return books;
    	} catch (Exception e) {
    		System.out.println("Connection Failed");
    		e.printStackTrace();
    		return new ArrayList<String>();
    	}
	}
	
	public String sumPrice() {
    	try {
    		Class.forName("org.postgresql.Driver");
    		c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library", "postgres", "postgres");
    		System.out.println( "Open Successfully!" );
    		st = c.createStatement(); String sum = null;
    		ResultSet rs = st.executeQuery("SELECT SUM(cost) FROM books");
    		while(rs.next()) {

    			sum = rs.getString(1);
    		}
    		System.out.println("sum: " + sum);
    		rs.close();
    		st.close();
    		c.close();
    		return sum;
    	} catch (Exception e) {
    		System.out.println("Connection Failed");
    		e.printStackTrace();
    		return "fail";
    	}
	}
	
	public String sumReserved() {
    	try {
    		Class.forName("org.postgresql.Driver");
    		c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library", "postgres", "postgres");
    		System.out.println( "Open Successfully!" );
    		st = c.createStatement(); String sum = null;
    		ResultSet rs = st.executeQuery("SELECT SUM(cost) FROM books, orders  WHERE orders.book_id = books.id");
    		while(rs.next()) {
    			sum = rs.getString(1);
    			
    		}
    		System.out.println("sum: " + sum);
    		rs.close();
    		st.close();
    		c.close();
    		return sum;
    	} catch (Exception e) {
    		System.out.println("Connection Failed");
    		e.printStackTrace();
    		return "fail";
    	}
	}
	
	public String sumPriceStel() {
    	try {
    		Class.forName("org.postgresql.Driver");
    		c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library", "postgres", "postgres");
    		System.out.println( "Open Successfully!" );
    		st = c.createStatement(); String sum = null;
    		ResultSet rs = st.executeQuery("SELECT SUM(cost) FROM books WHERE location = 'стелаж'");
    		while(rs.next()) {
    			sum = rs.getString(1);
    		}
    		System.out.println("id: " + sum);
    		rs.close();
    		st.close();
    		c.close();
    		return sum;
    	} catch (Exception e) {
    		System.out.println("Connection Failed");
    		e.printStackTrace();
    		return "Fail";
    	}
	}
	
	public String createOrder (String student_id, String book_name, String user, boolean avaliable, boolean student) {
		if (avaliable == true && student == true) {
		try {
    		Class.forName("org.postgresql.Driver");
    		c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library", "postgres", "postgres");
    		System.out.println( "Open Successfully!" );
    		st = c.createStatement(); 
    		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
 		   LocalDateTime now = LocalDateTime.now();  
 		   String issue_date = dtf.format(now);
 		   ResultSet rs = st.executeQuery("SELECT id FROM books WHERE book_name = "+"'"+book_name+"'");
 		  String book_id = null;
 		  while(rs.next()) {
  			book_id = rs.getString("id");
 		  	}
 		   
    		String sql = "INSERT INTO orders(student_id,book_id,librarian,issue_date) "+ "VALUES(" 
    		+ "'"+student_id + "'"+"," + "'"+book_id+"'"+","+ "'"+ user +"'"+ "," + "'"+issue_date + "'"+ ")";
    		st.executeUpdate(sql);
    		st.close();
    		c.close();
    		Location l = new Location("bolt://localhost:7687", "neo4j", "neo4jn");
    		String loc = l.findLocation(book_id);
    		return loc;
    	} catch (Exception e) {
    		System.out.println("Connection Failed");
    		e.printStackTrace();
    		return "fail connection";
    	}
		}
		else if(avaliable == false) {
			return "Book already reserved";
		}else {
			return "Student has 10 books";
		}
	}
	public boolean checkBook(String name) {
		String id = null;
		try {
    		Class.forName("org.postgresql.Driver");
    		c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library", "postgres", "postgres");
    		System.out.println( "Open Successfully!" );
    		st = c.createStatement(); 
    		ResultSet rs = st.executeQuery("SELECT id FROM books WHERE book_name = " +"'" +name +"'");
    		while(rs.next()) {
    			id = rs.getString("id");
    		}
    		System.out.println("id: " + id);
    		rs.close();
    		String r = null;
    		ResultSet rslt = st.executeQuery("SELECT id FROM orders WHERE book_id = " +"'" +id +"'"+ "AND back_date IS NULL");
    		while(rslt.next()) {
    			r = rslt.getString("id");
    		}
    		rslt.close();
    		st.close();
    		c.close();
    		if(r != null) {
    			return false; // book taken
    		}else {
    			return true; // book avaliable
    			
    		}
    	} catch (Exception e) {
    		System.out.println("Connection Failed");
    		e.printStackTrace();
    		return false;
    	}
	}
	
	public String takeBackBook(String id) {
		
		try {
    		Class.forName("org.postgresql.Driver");
    		c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/library", "postgres", "postgres");
    		System.out.println( "Open Successfully!" );
    		st = c.createStatement(); 
    		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
 		   LocalDateTime now = LocalDateTime.now();  
 		   String s = dtf.format(now);
    		String sql = "UPDATE orders SET back_date = " + "'"+s + "'"+ " WHERE book_id = " + "'"+id + "'" + " AND back_date IS NULL";
    		st.executeUpdate(sql);
    		st.close();
    		c.close();
    		return "OK";
    	} catch (Exception e) {
    		System.out.println("Connection Failed");
    		e.printStackTrace();
    		return "Problem";
    	}
		
	}
    public static void main( String[] args ) throws SQLException{
    
    }
}
