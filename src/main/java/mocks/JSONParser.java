package mocks;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import main.User;

public class JSONParser {
	
	public static User[] fetchRegisteredUsers(String file) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(new FileInputStream(file), User[].class );		
	}
	
	public static void addRegisteredUser(String file, User user) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(new FileOutputStream(file), user );		
	}	
}
