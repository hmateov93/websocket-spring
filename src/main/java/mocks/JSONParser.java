package mocks;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import main.User;

public class JSONParser {
	
	private static String usersjson = "src/main/java/mocks/users.json";
	
	public static User[] fetchRegisteredUsers() throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(new FileInputStream(usersjson), User[].class );		
	}
	
	public static void writeRegisteredUsers(User[] users) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(new FileOutputStream(usersjson), users );	
	}	
}
