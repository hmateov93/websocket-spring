package main;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import mocks.JSONParser;

@Controller
public class LobbyController {

	public Room[] rooms;
	
	public LobbyController(){
		try {
			rooms = JSONParser.fetchRooms();
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
    
    @SuppressWarnings("unused")
	private Room[] createNewArrayFromOld(Room[]rooms, Room newroom){
    	Room[] newroomsarray=new Room[rooms.length+1];
    	for(int i=0;i<rooms.length;i++){
    		newroomsarray[i]=rooms[i];
    	}
    	newroomsarray[newroomsarray.length-1]=newroom;
    	return newroomsarray;
    }    

}
