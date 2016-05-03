package main;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import domain.User;
import mocks.JSONParser;

@Controller
public class LoginController {

    @MessageMapping("/login")
    @SendTo("/topic/login")
    public User checkUser(User user) throws Exception {
    	Thread.sleep(100); // simulated delay
    	boolean userexists=false;
    	User newuser=null;
    	User[] users = JSONParser.fetchRegisteredUsers();
    	for(int i=0;i<users.length;i++){   	
    		if(users[i].getName().equals(user.getName())){
    			userexists=true;
    			if(users[i].getPassword().equals(user.getPassword())){
    				newuser=users[i];
    			}
    		}
    	}
        if(newuser==null && userexists==false){
        	newuser=user;
        	newuser.setType("USER");
        	JSONParser.writeRegisteredUsers(createNewArrayFromOld(users,newuser));
        }
        return newuser;
    }
    
    
    private User[] createNewArrayFromOld(User[]users,User newuser){
    	User[] newuserarray=new User[users.length+1];
    	for(int i=0;i<users.length;i++){
    		newuserarray[i]=users[i];
    	}
    	newuserarray[newuserarray.length-1]=newuser;
    	return newuserarray;
    }

}
