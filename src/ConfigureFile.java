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
/*public void Create_JSON_Configure_File(ConfigureFile obj) {
//Crit_demo_sex_data crit1 = new Crit_demo_sex_data("demo_sex","SEX-01");
	//Crit_demo_ethnicity_data crit2 = new Crit_demo_ethnicity_data("demo_ethnicity","ETHN-05");
	//Crit_demo_education_level_data crit3 = new Crit_demo_education_level_data("demo_education","EDU-LEV-03");
	//ArrayList<Criterion> cur_list_of_criterions = new ArrayList<Criterion>();
	//cur_list_of_criterions.add(crit1);
	//cur_list_of_criterions.add(crit2);
	//cur_list_of_criterions.add(crit3);

	//ConfigureFile obj = new ConfigureFile();
//obj.setList_of_criterions(obj);
ObjectMapper mapper = new ObjectMapper();
mapper.enableDefaultTyping();
mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
mapper.disable(Feature.FAIL_ON_EMPTY_BEANS);
try {
	String res=mapper.writeValueAsString(obj);
	System.out.println("The new json file that we created is: "+ res);
} catch (JsonGenerationException e1) {
	e1.printStackTrace();
} catch (JsonMappingException e1) {
	e1.printStackTrace();
} catch (IOException e1) {
	e1.printStackTrace();
}

}
public static ConfigureFile From_JSON_String_to_ConfigureFile(String inputJSON) {
	ConfigureFile result=null;
	//Conatiner_of_Criterions results=null;
	ObjectMapper mapper = new ObjectMapper();
	mapper.enableDefaultTyping();
	
	try {
		ConfigureFile obj = mapper.readValue(inputJSON, ConfigureFile.class);
		result=obj;
	} catch (JsonParseException e1) {
		e1.printStackTrace();
	} catch (JsonMappingException e1) {
		e1.printStackTrace();
	} catch (IOException e1) {
		e1.printStackTrace();
	}
	
	return result;
}*/

}

