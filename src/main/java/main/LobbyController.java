package main;

import java.util.ArrayList;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class LobbyController {

	public ArrayList<Room>rooms = new ArrayList<Room>();
	
	public LobbyController(){
		mockRooms();
	}
	
	private void mockRooms(){
		rooms.add(new Room("A room"));
		rooms.add(new Room("Another room"));
		rooms.add(new Room("Users in here"));
		rooms.add(new Room("Arghh pirates only"));
	}


    @MessageMapping("/lobby")
    @SendTo("/topic/rooms")
    public Object[] requestRooms(String message) throws Exception {
    	Thread.sleep(100); // simulated delay
        return rooms.toArray();
    }	

}
