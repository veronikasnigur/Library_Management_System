package project.library;

import java.net.MalformedURLException;
import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.ViewResult.Row;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

public class AppUser {
	public String register(String password, String nameSurname, String dob, String phone) throws MalformedURLException {
		HttpClient httpClient = new StdHttpClient.Builder().
    		    url("http://localhost:5984").
    		    username("couchdb").
    		    password("couchdb").
    		    build();

    		CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
    		CouchDbConnector db = dbInstance.createConnector("librarians", true);
    		System.out.println("connected");
    		User user = new User(password,nameSurname,dob,phone);
    		db.create(user);
    		return "User registred";
	}
	public boolean authorization(String password) throws MalformedURLException {
		HttpClient httpClient = new StdHttpClient.Builder().
    		    url("http://localhost:5984").
    		    username("couchdb").
    		    password("couchdb").
    		    build();

    		CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
    		CouchDbConnector db = dbInstance.createConnector("librarians", true);
    		System.out.println("connected");
    		ViewQuery query = new ViewQuery() 
                    .designDocId( "_design/User" ) 
                    .viewName( "find_password" ) 
                    .key( password ); 
               
    		ViewResult doc = db.queryView(query);
    		List<Row> l = doc.getRows();
    		String rs = null;
    		for(Row row:l) {
    			rs = row.toString();
    			System.out.println(rs);
    		}
    		if(rs == null) {
    			//System.out.println("wrong password");
    			return false;
    		}else {
    			//System.out.println("right password");
    			return true;
    		}
	}
}
