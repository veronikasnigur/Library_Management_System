package project.library;

import static org.neo4j.driver.Values.parameters;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import static org.neo4j.driver.Values.parameters;




public class Location implements AutoCloseable {
private final Driver driver;

    public Location(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }
    @Override
    public void close() throws RuntimeException {
        driver.close();
    }
    @SuppressWarnings("deprecation")
    public void addBookLocation(final int number, final String bookId, final String name) {
    	try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
    		tx.run("MERGE (a:Book {bookcase_number: $number, id: $bookId}) "
    				+ "MERGE (b:Location {name: $name}) "
    				+ "CREATE (a)-[r:located]->(b)", 
    				parameters("number", number, "bookId",bookId, "name",name));
                return 1;
            });
        }
        
    }
    @SuppressWarnings("deprecation")
    public String findLocation(final String id){
    	try (Session session = driver.session()) {
            List<Record> ways = session.readTransaction(new TransactionWork<List<Record>>() {
                @Override
                public List<Record> execute(Transaction tx) {
                	return tx.run("MATCH p=(a:Book{id: $id})-[:located]->(b:Location) "
                    		+ "RETURN a.bookcase_number AS number, b.name AS location", parameters("id",id)).list() ;
                    
                }
            });
            String s = "";
            for (final Record way : ways) {
            	s   += "Місцезнаходження : " +way.get("location").toString()+ " номер шафи: "+way.get("number").toString();
            	
            }
            return s;
    	}
    	
    }
    public static void main(String[] args) {
    	try (Location book = new Location("bolt://localhost:7687", "neo4j", "neo4jn")) {
    		System.out.print( book.findLocation("60472"));
    		//book.addBookAndAuthor(4, 28541, "стелаж");
    	}
    }
}
