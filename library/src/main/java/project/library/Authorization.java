package project.library;
import java.time.format.DateTimeFormatter;

import redis.clients.jedis.Jedis;

import java.time.LocalDateTime;  
public class Authorization {

	public void logIn(String password) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
		   LocalDateTime now = LocalDateTime.now();  
		   String s = dtf.format(now);
		   Jedis jedis = new Jedis("localhost", 6379);
		   jedis.hset(password, "logintime", s);
		  
		   
	}
	public void logOut(String password) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
		   LocalDateTime now = LocalDateTime.now();  
		   String s = dtf.format(now);
		   Jedis jedis = new Jedis("localhost", 6379);
		   jedis.hset(password, "logouttime", s);
		   
		   
	}
	public static void main(String[] args) {
	}

}
