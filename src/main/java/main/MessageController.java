package main;
import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import mocks.JSONParser;

@Controller
public class MessageController {
	
	public ArrayList<ArrayList<User>>users = new ArrayList<ArrayList<User>>();
	
	public MessageController(){
		createUserLists();
	}
	
    @MessageMapping("/chat/{chatId}")
    @SendTo("/topic/messages/{chatId}")
    public Message message(MessageContent message, @DestinationVariable String chatId) throws Exception {
        Thread.sleep(100); // simulated delay
        Message finalmessage=null;
        if(message.type==0)finalmessage = new Message(message.getUser() + " : " + message.getContent(), null); //Plain communication between users
        else if(message.type==1){ //New user joining the channel
        	User user = findUserFromName(JSONParser.fetchRegisteredUsers(), message.getUser());
        	if(user!=null){
        		if(findUserFromName(chatId,user.name)!=9999){
        			users.get(Integer.parseInt(chatId)).remove(findUserFromName(chatId,user.name));
        		}	
        		users.get(Integer.parseInt(chatId)).add(user);
        		User[] tempusers = users.get(Integer.parseInt(chatId)).toArray(new User[users.get(Integer.parseInt(chatId)).size()]);
        		String finallist = JSONParser.stringifyUserlist(tempusers);
        		finalmessage = new Message(message.getUser() + " joined the channel", finallist); 
        	}
        	else finalmessage = new Message("ERROR", null); 
        }
        else if(message.type==2){ //User leaving the channel
        	User user = findUserFromName(JSONParser.fetchRegisteredUsers(), message.getUser());
        	if(user!=null){
        		if(findUserFromName(chatId,user.name)!=9999){
        			users.get(Integer.parseInt(chatId)).remove(findUserFromName(chatId,user.name));
        		}	
        		User[] tempusers = users.get(Integer.parseInt(chatId)).toArray(new User[users.get(Integer.parseInt(chatId)).size()]);
        		String finallist = JSONParser.stringifyUserlist(tempusers);
        		finalmessage = new Message(message.getUser() + " left the channel", finallist);  
        	}
        	else finalmessage = new Message("ERROR", null);         	
        }
        return finalmessage;
    }
    
    private void createUserLists(){
    	try {
			int roomnumber = JSONParser.fetchRooms().length;
			for(int i=0;i<roomnumber;i++){
				users.add(new ArrayList<User>());
			}
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    //We use this method when dealing with the arraylist
    private int findUserFromName(String chatId, String name){
    	int index = 9999;
    	for(int i=0;i<users.get(Integer.parseInt(chatId)).size();i++){
    		if(users.get(Integer.parseInt(chatId)).get(i).getName().equals(name))index=i;
    	}
    	return index;
    }     
    
    //We use this method when dealing with JSONs simple arrays
    private User findUserFromName(User[]users, String name){ 
    	User user = null;
    	for(int i=0;i<users.length;i++){
    		if(users[i].getName().equals(name))user=users[i];
    	}
    	return user;
    }
    
}