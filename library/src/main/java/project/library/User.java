package project.library;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
	public String id;
	public String password; //= "anna12lr";
	public String name_surname; //= "Петрова Анна Семенівна";
	public String birth_date;// = "08.05.1991";
	public String phone_number;// = "+380997125483";
    private String revision;
    //private String color;
    public User (String newPassword, String newNameSurname, String newBirthDate, String newPhoneNumber) {
    	password = newPassword;
    	name_surname = newNameSurname;
    	birth_date = newBirthDate;
    	phone_number = newPhoneNumber;
    }
    @JsonProperty("_id")
    public String getId() {
            return id;
    }

    @JsonProperty("_id")
    public void setId(String s) {
            id = s;
    }

    @JsonProperty("_rev")
    public String getRevision() {
            return revision;
    }

    @JsonProperty("_rev")
    public void setRevision(String s) {
            revision = s;
    }
}
