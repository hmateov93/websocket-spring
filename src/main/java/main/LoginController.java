package main;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import domain.EncapsulatedUser;
import domain.User;
import mocks.JSONParser;

@Controller
public class LoginController {

    @MessageMapping("/login/{clientId}")
    @SendTo("/topic/login/{clientId}")
    public EncapsulatedUser checkUser(User user) throws Exception {
    	boolean userexists=false;
    	User newuser=null;
    	User[] users = JSONParser.fetchRegisteredUsers();
    	for(int i=0;i<users.length;i++){   	
    		if(users[i].getName().equals(user.getName())){
    			userexists=true;
    			if(users[i].getPassword().equals(user.getPassword())){
    				newuser=users[i];
    				if(newuser.getStatus().equals("BANNED")){
    					return new EncapsulatedUser("BANNED", newuser);
    				}
    				return new EncapsulatedUser("OK", newuser);
    			}
    		}
    	}
        if(newuser==null && userexists==false){
        	newuser=user;
        	newuser.setType("USER");
        	if(!newuser.getName().equals("")){
            	JSONParser.writeRegisteredUsers(createNewArrayFromOld(users,newuser));
            	return new EncapsulatedUser("OK", newuser);        		
        	}
        	else{
        		return new EncapsulatedUser("EMPTY_USERNAME", newuser);
        	}
        	
        }
        return new EncapsulatedUser("INVALID_PASSWORD", newuser);
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
