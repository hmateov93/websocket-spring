package main;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import mocks.JSONParser;

@Controller
public class LoginController {
	
	private String usersjson = "src/main/java/mocks/users.json";

    @MessageMapping("/login")
    @SendTo("/topic/login")
    public User checkUser(User user) throws Exception {
    	Thread.sleep(100); // simulated delay
    	User newuser=null;
    	User[] users = JSONParser.fetchRegisteredUsers(usersjson);
    	for(int i=0;i<users.length;i++){   		
    		if(users[i].name.equals(user.name) && users[i].password.equals(user.password)){
    			newuser=users[i];
    		}
    	}
        if(newuser==null){
        	newuser=user;
        	newuser.type="USER";
        	JSONParser.addRegisteredUser(usersjson, newuser);
        }
        
        return newuser;
    }

}
