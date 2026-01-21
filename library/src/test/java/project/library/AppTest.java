package project.library;

//import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.net.MalformedURLException;
import java.sql.SQLException;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
	App app = new App();
	AppUser apUs = new AppUser();
	@Test
    public void checkAddingBook() throws SQLException
    {
    	assertEquals("Book Added", app.addNewBook("28541", "Роберт Мартін", "Чиста архітектура", "2020", "Фабула", "Харків", "абонемент",2, "688"));
    }
    @Test
    public void checkRightOrderCreating() throws SQLException
    {
    	assertEquals("order created", app.createOrder("2", "Замок", "o1978shew", true, true));
    }
    @Test
    public void checkBookFalseOrderCreating() throws SQLException
    {
    	assertEquals("Book already reserved", app.createOrder("2", "Як працює Google", "o1978shew", false, true));
    }
    @Test
    public void checkStudentFalseOrderCreating() throws SQLException
    {
    	assertEquals("Student has 10 books", app.createOrder("2", "Як працює Google", "o1978shew", true, false));
    }
    @Test
    public void checkBothFalseOrderCreating() throws SQLException
    {
    	assertEquals("Book already reserved", app.createOrder("2", "Як працює Google", "o1978shew", false, false));
    }
   @Test
    public void checkBookNotTaken() throws SQLException
    {
    	assertEquals(true, app.checkBook("Гаррі Поттер і філософський камінь"));
    }
    @Test
    public void checkBookTaken() throws SQLException
    {
    	assertEquals(false, app.checkBook("Як працює Google"));
    }
    @Test
    public void checkRightAuthorization() throws MalformedURLException
    {
    	assertEquals("right password", apUs.authorization("anna12lr"));
    }
    @Test
    public void checkWrongAuthorization() throws MalformedURLException
    {
    	assertEquals("wrong password", apUs.authorization("Bnna12lr"));
    }
    @Test
    public void checkRegistration() throws MalformedURLException
    {
    	assertEquals("User registred", apUs.register("anOlks19", "Богданова Анна Олександрівна", "12.04.1999", "+380958324651"));
    }
}
