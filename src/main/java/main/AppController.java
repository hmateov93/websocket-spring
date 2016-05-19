package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
public class AppController {

	//TODO: Change primitive array to arraylist
	public ArrayList<Room> rooms;
	public HashMap<String, ArrayList<User>>users;
	
	public AppController(){
		try {
			rooms = roomArrayToArrayList(JSONParser.fetchRooms());
			users = new HashMap<String, ArrayList<User>>();
			for(int i=0;i<rooms.size();i++){
				users.put(""+rooms.get(i).getId(),new ArrayList<User>());
			}
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
        return roomArrayListToArray(rooms);
    }	
    
    @MessageMapping("/createRoom")
    @SendTo("/topic/rooms")
    public Room[] createRoom(Room room) throws Exception {
    	room.setId(findNewId(0));
    	this.rooms.add(room);
    	JSONParser.writeRooms(roomArrayListToArray(rooms));
    	users.put(""+room.getId(), new ArrayList<User>());
        return roomArrayListToArray(rooms);
    }	    
    
    @MessageMapping("/deleteRoom")
    @SendTo("/topic/rooms")
    public Room[] deleteRoom(String message) throws Exception {
    	this.rooms.remove(findRoomFromId(Integer.parseInt(message)));
    	JSONParser.writeRooms(roomArrayListToArray(rooms));
    	users.remove(message);
        return roomArrayListToArray(rooms);
    }	 
    
    @MessageMapping("/createUser")
    @SendTo("/topic/users")
    public User[] createUser(User user) throws Exception {
    	ArrayList<User>users = null;
    		users = userArrayToArrayList(JSONParser.fetchRegisteredUsers());
        	users.add(user);    	
        	JSONParser.writeRegisteredUsers(userArrayListToArray(users));
            return userArrayListToArray(users);
    }	
    
    @MessageMapping("/deleteUser")
    @SendTo("/topic/users")
    public User[] deleteUser(String name) throws Exception {
    	ArrayList<User>users = null;
		users = userArrayToArrayList(JSONParser.fetchRegisteredUsers());
		for(int i=0;i<users.size();i++){
			if(name.equals(users.get(i).getName())){
				users.remove(users.get(i));  
			}
		}	  	
		JSONParser.writeRegisteredUsers(userArrayListToArray(users));
        return userArrayListToArray(users);
    }    
    
    @MessageMapping("/users")
    @SendTo("/topic/users")
    public User[] getUsers(User user) throws Exception {
    	User[]users = null;
    	if(user.getType().equals("ADMIN")){
    		users = JSONParser.fetchRegisteredUsers();
    	}
        return users;
    }	    
    
    @MessageMapping("/chat/{chatId}")
    @SendTo("/topic/messages/{chatId}")
    public Message message(MessageContent message, @DestinationVariable String chatId) throws Exception {
        Message finalmessage=null;
        
        //Plain communication between users
        if(message.type==0)finalmessage = new Message(message.getUser() + " : " + message.getContent(), null,true); 
     
        //New user joining the channel
        else if(message.type==1){ 
        	User user = findUserFromName(JSONParser.fetchRegisteredUsers(), message.getUser());
        	if(user!=null && users.get(chatId)!=null){
        		if(findUserFromName(chatId,user.getName())!=9999){
        			users.get(chatId).remove(findUserFromName(chatId,user.getName()));
        		}	
        		users.get(chatId).add(user);
        		User[] tempusers = users.get(chatId).toArray(new User[users.get(chatId).size()]);
        		String finallist = JSONParser.stringifyUserlist(tempusers);
        		finalmessage = new Message(message.getUser() + " joined the channel", finallist, true); 
        	}
        	else finalmessage = new Message("ERROR", null,false); 
        }
        
        //User leaving the channel
        else if(message.type==2){ 
        	User user = findUserFromName(JSONParser.fetchRegisteredUsers(), message.getUser());
        	if(user!=null && users.get(chatId)!=null){
        		if(findUserFromName(chatId,user.getName())!=9999){
        			users.get(chatId).remove(findUserFromName(chatId,user.getName()));
        		}	
        		User[] tempusers = users.get(chatId).toArray(new User[users.get(chatId).size()]);
        		String finallist = JSONParser.stringifyUserlist(tempusers);
        		finalmessage = new Message(message.getUser() + " left the channel", finallist, true);  
        	}
        	else finalmessage = new Message("ERROR", null, false);         	
        }
        
        if(users.get(chatId)==null)finalmessage = new Message("ERROR", null, false);
        return finalmessage;
    }

    
    private int findNewId(int id){
    	if(!checkIdAvailable(id)){
    		return id;
    	}
    	else return findNewId(id+1);
    }
    
    private boolean checkIdAvailable(int id){
    	boolean found=false;
    	for(int i=0;i<rooms.size();i++){
    		if(id==rooms.get(i).getId())found=true;
    	}
    	return found;
    }
      
	
	private ArrayList<Room> roomArrayToArrayList(Room[]rooms){
		ArrayList<Room>roomslist = new ArrayList<Room>();
		for(int i=0;i<rooms.length;i++){
			roomslist.add(rooms[i]);
		}
		return roomslist;
	}
  
	private Room[] roomArrayListToArray(ArrayList<Room>rooms){
		Room[]roomsarray = new Room[rooms.size()];
		for(int i=0;i<rooms.size();i++){
			roomsarray[i] = rooms.get(i);
		}
		return roomsarray;
	}
	
	private ArrayList<User> userArrayToArrayList(User[]users){
		ArrayList<User>roomslist = new ArrayList<User>();
		for(int i=0;i<users.length;i++){
			roomslist.add(users[i]);
		}
		return roomslist;
	}
  
	private User[] userArrayListToArray(ArrayList<User>users){
		User[]roomsarray = new User[users.size()];
		for(int i=0;i<users.size();i++){
			roomsarray[i] = users.get(i);
		}
		return roomsarray;
	}	
	
	private int findRoomFromId(int id){
		int index = 9999;
		for(int i=0;i<rooms.size();i++){
			if(rooms.get(i).getId()==id)index=i;
		}
		return index;
	}
	
    //We use this method when dealing with the arraylist
    private int findUserFromName(String chatId, String name){
    	int index = 9999;
    	for(int i=0;i<users.get(chatId).size();i++){
    		if(users.get(chatId).get(i).getName().equals(name))index=i;
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
