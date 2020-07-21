import java.io.IOException;


/*import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonMethod;

import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;*/




public class ConfigureFile {
	
public String dbURL; 		//="jdbc:mysql://localhost:3306/HarmonicSS";
public  String username; 	//="root"; //@localhost";
public  String password; 	//"ordo12345";

public ConfigureFile() {
	this.dbURL = "";
	this.username = "";
	this.password = "";
}
public ConfigureFile(String dbURL, String username, String password) {
	this.dbURL = dbURL;
	this.username = username;
	this.password=password;
}


public String getDbURL() {
	return dbURL;
}
public void setDbURL(String dbURL) {
	this.dbURL = dbURL;
}
public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}

}

