package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import domain.Message;
import domain.MessageContent;
import domain.Room;
import domain.User;
import mocks.JSONParser;

@Controller
public class LobbyController {

	//TODO: Change primitive array to arraylist
	public Room[] rooms;
	public HashMap<String, ArrayList<User>>users = new HashMap<String, ArrayList<User>>();
	
	public LobbyController(){
		try {
			rooms = JSONParser.fetchRooms();
			createUserLists();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    @MessageMapping("/lobby")
    @SendTo("/topic/rooms")
    public Room[] requestRooms(String message) throws Exception {
    	Thread.sleep(100); // simulated delay
        return rooms;
    }	
    
    @MessageMapping("/createRoom")
    @SendTo("/topic/rooms")
    public Room[] createRoom(Room room) throws Exception {
    	Thread.sleep(100); // simulated delay
    	this.rooms = addRoomToArray(room);
    	JSONParser.writeRooms(rooms);
    	users.put(""+room.getId(), new ArrayList<User>());
        return rooms;
    }	    
    
    @MessageMapping("/deleteRoom")
    @SendTo("/topic/rooms")
    public Room[] deleteRoom(String message) throws Exception {
    	Thread.sleep(100); // simulated delay
    	this.rooms = deleteRoomFromArray(Integer.parseInt(message));
    	JSONParser.writeRooms(rooms);
    	users.remove(""+Integer.parseInt(message));
        return rooms;
    }	       
    
	private Room[] addRoomToArray(Room newroom){
    	Room[] newroomsarray=new Room[rooms.length+1];
    	for(int i=0;i<rooms.length;i++){
    		newroomsarray[i]=rooms[i];
    	}
    	newroom.setId(newroomsarray.length-1);
    	newroomsarray[newroomsarray.length-1]=newroom;
    	return newroomsarray;
    }    
    
	
	private Room[] deleteRoomFromArray(int index){
    	Room[] newroomsarray=new Room[rooms.length-1];
    	int newarrayindex = 0;
    	for(int i=0;i<rooms.length;i++){
    		if(rooms[i].getId()!=index){
    			newroomsarray[newarrayindex]=rooms[i];
    			newarrayindex++;
    		}
    		
    	}
    	return newroomsarray;
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
        		if(findUserFromName(chatId,user.getName())!=9999){
        			users.get(Integer.parseInt(chatId)).remove(findUserFromName(chatId,user.getName()));
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
        		if(findUserFromName(chatId,user.getName())!=9999){
        			users.get(Integer.parseInt(chatId)).remove(findUserFromName(chatId,user.getName()));
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
				users.put(""+rooms[i].getId(),new ArrayList<User>());
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
