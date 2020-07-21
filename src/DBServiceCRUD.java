import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.json.JSONObject;

import java.sql.ResultSet;




public class DBServiceCRUD {
	static Connection db_con_obj = null;
	static PreparedStatement db_prep_obj = null;
	
	public static Boolean makeJDBCConnection(ConfigureFile configureFile_obj) {
		 Boolean connection_succes=true;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Congrats - Seems your MySQL JDBC Driver Registered!");
		} catch (ClassNotFoundException e) {
			System.out.println("Sorry, couldn't found JDBC driver. Make sure you have added JDBC Maven Dependency Correctly");
			e.printStackTrace();
			return false;
		}
 
		try {
			//System.out.println("URL: "+configureFile_obj.getDbURL()+" username: "+configureFile_obj.getUsername()+" password: "+configureFile_obj.getPassword());
			db_con_obj = DriverManager.getConnection(configureFile_obj.getDbURL(), configureFile_obj.getUsername(), configureFile_obj.getPassword());
			//db_con_obj = DriverManager.getConnection("jdbc:mysql://147.102.19.66:3306/HarmonicSS","ponte", "p0nt3");
			if (db_con_obj != null) {
				System.out.println("Connection Successful! Enjoy. Now it's time to Store data");
			} else {
				System.out.println("Failed to make connection!");
				return false;
			}
		} catch (SQLException e) {
			System.out.println("MySQL Connection Failed!");
			e.printStackTrace();
			return false;
		}
		return connection_succes;
	}
	
	public static void closeJDBCConnection() throws SQLException {
		 if (db_prep_obj != null) {
	        	db_prep_obj.close();
	        }
	        if (db_con_obj != null) {
	        	db_con_obj.close();
	        }
	        System.out.println("DB Connection closed successfully");
	}
	
	public static boolean testQuery(String getQueryStatement) throws SQLException {
		//String query_results=""; 
		
			//System.out.println("Stage-1 Prepare statement.");
			db_prep_obj = db_con_obj.prepareStatement(getQueryStatement);
			
			//System.out.println("Stage-2 Ready to execute the query.");
			// Execute the Query, and get a java ResultSet
			
			ResultSet rs = db_prep_obj.executeQuery();
			//System.out.println("We are ready to retrieve data from DB.");
			
			//System.out.println("Stage-3 The query is executed.");
			// Let's iterate through the java ResultSet
			if (!rs.next()) return false;
			
			return true;
	}
	
	public static String assistanceQuery(String getQueryStatement, boolean essdai, boolean caci) throws SQLException {
		String termsFound = "";
		db_prep_obj = db_con_obj.prepareStatement(getQueryStatement);
		ResultSet rs = db_prep_obj.executeQuery();
		if(essdai) {
			while(rs.next()) {
				if(termsFound.equals("")) termsFound = rs.getString("DOMAIN_NAME");
				else termsFound += ", " + rs.getString("DOMAIN_NAME");
			}
		}
		else if(caci) {
			while(rs.next()) {
				if(termsFound.equals("")) termsFound = rs.getString("CONDITION");
				else termsFound += ", " + rs.getString("CONDITION");
			}
		}
		else {
			while(rs.next()) {
				if(termsFound.equals("")) termsFound = rs.getString("NAME");
				else termsFound += ", " + rs.getString("NAME");
			}
		}
		return termsFound;
	}
	
	public static String getIDsFromDB(String getQueryStatement) throws SQLException {
		String query_results=""; 
		
		//System.out.println("Stage-1 Prepare statement.");
		db_prep_obj = db_con_obj.prepareStatement(getQueryStatement);
		
		//System.out.println("Stage-2 Ready to execute the query.");
		// Execute the Query, and get a java ResultSet
		
		ResultSet rs = db_prep_obj.executeQuery();
		//System.out.println("We are ready to retrieve data from DB.");
		
		//System.out.println("Stage-3 The query is executed.");
		// Let's iterate through the java ResultSet
		if (rs == null) return "";
		boolean mycount = false;
		while (rs.next()) {
			//System.out.println("Stage-4 We retrieve the IDs one by one.");
			String id = rs.getString("ID");
			if(!mycount) {
				query_results=id;
				mycount=true;
			}
			else query_results+=","+id;//Integer.toString(id) + " ";//+Float.toString(value)+". ";
			
		}//System.out.println("Stage-5 Finished.");
		return query_results;
	}
	
	public static String getDataFromDB(String getQueryStatement) throws SQLException {
		String query_results=""; 
		
			//System.out.println("Stage-1 Prepare statement.");
			db_prep_obj = db_con_obj.prepareStatement(getQueryStatement);
			
			//System.out.println("Stage-2 Ready to execute the query.");
			// Execute the Query, and get a java ResultSet
			
			ResultSet rs = db_prep_obj.executeQuery();
			//System.out.println("We are ready to retrieve data from DB.");
			
			//System.out.println("Stage-3 The query is executed.");
			// Let's iterate through the java ResultSet
			if (rs == null) return "";
			boolean mycount = false;
			String id = "";
			String term = "";
			while (rs.next()) {
				//System.out.println("Stage-4 We retrieve the IDs one by one.");
				String myid = rs.getString("ID");
				String myterm = rs.getString("NAME");
				if(!id.equals(myid)) {
					id = myid;
					if(query_results.equals("")) query_results += id+"!";
					else query_results += term+"|"+id+"!";
					term = myterm;
				}
				else {
					term += ","+myterm;
				}				
			}
			query_results += term;
			
			//System.out.println("Stage-5 Finished.");
			return query_results;
	}
	
	public static String getUIDDataFromDB(String getQueryStatement) throws SQLException {
		String query_results=""; 
		
			//System.out.println("Stage-1 Prepare statement.");
			db_prep_obj = db_con_obj.prepareStatement(getQueryStatement);
			
			//System.out.println("Stage-2 Ready to execute the query.");
			// Execute the Query, and get a java ResultSet
			
			ResultSet rs = db_prep_obj.executeQuery();
			//System.out.println("We are ready to retrieve data from DB.");
			
			//System.out.println("Stage-3 The query is executed.");
			// Let's iterate through the java ResultSet
			if (rs == null) return "";
			boolean mycount = false;
			while (rs.next()) {
				//System.out.println("Stage-4 We retrieve the IDs one by one.");
				String id = rs.getString("UID");//.getInt("ID");
				//Float value = rs.getFloat("VALUE");//  .getString("Address");
				if(!mycount) {
					query_results=id;
					mycount=true;
				}
				else query_results+=","+id;//Integer.toString(id) + " ";//+Float.toString(value)+". ";
				
			}//System.out.println("Stage-5 Finished.");
			return query_results;
	}
	
	public static void getXMLRequestFromDB(String requestID) throws SQLException, IOException {
		String query = "SELECT REQUEST_XML FROM EXECUTION_DATA WHERE REQUEST_ID='" + requestID +"'";
		db_prep_obj = db_con_obj.prepareStatement(query);
		ResultSet rs = db_prep_obj.executeQuery();
		//File fXmlFile = new File(requestID+".xml");
		/*FileOutputStream fos = new FileOutputStream(fXmlFile);
		System.out.println("Writing BLOB to file " + fXmlFile.getAbsolutePath());
        while (rs.next()) {
            InputStream input = rs.getBinaryStream("REQUEST_XML");
            byte[] buffer = new byte[1024];
            while (input.read(buffer) > 0) {
                fos.write(buffer);
            }
        }*/
		FileWriter fw = new FileWriter(requestID + ".xml");
		 while (rs.next()) {
			 fw.write(rs.getString("REQUEST_XML"));
			 fw.close();
		 }
		
        if (rs != null) {
            rs.close();
        }
        if (db_prep_obj != null) {
        	db_prep_obj.close();
        }
        if (db_con_obj != null) {
        	db_con_obj.close();
        }
        /*if (fos != null) {
            fos.close();
        }*/
		//return fXmlFile;
	}
	
	/*public static void setPatientsIDs(String darID, Result_UIDs results) throws SQLException {
		Date date = new Date();
		Object param = new java.sql.Timestamp(date.getTime());
		String query = "INSERT INTO Eligible_Patient_IDs (DAR_ID, EXEC_DATE, PATIENT_IDS) VALUES (?, ?, ?)";
		db_prep_obj = db_con_obj.prepareStatement(query);
		db_prep_obj.setString(1, darID);
		db_prep_obj.setTimestamp(2, (Timestamp) param);
		if(results.UIDs_defined_ALL_elements.length==1 && results.UIDs_defined_ALL_elements[0].equals("")) db_prep_obj.setString(3, "");
		else {
			String dbstring = "";
			String[] idstring = new String[results.UIDs_defined_ALL_elements.length];
			for(int k=0; k<results.UIDs_defined_ALL_elements.length; k++) {
			
				idstring[k]=results.UIDs_defined_ALL_elements[k];
			}
			JSONObject dbstring = new JSONObject();
			dbstring.put("IDs", idstring);
			db_prep_obj.setString(3, dbstring.toString());
		}
		db_prep_obj.execute();
	}*/
	
	
	public static void main(String [] args) {

	}
}
