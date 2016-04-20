package main;
import java.util.ArrayList;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {
	
	public ArrayList<User>users = new ArrayList<User>();
	
    @MessageMapping("/chat/{chatId}")
    @SendTo("/topic/messages/{chatId}")
    public Message message(MessageContent message, @DestinationVariable String chatId) throws Exception {
        Thread.sleep(100); // simulated delay
        Message finalmessage=null;
        if(message.type==0)finalmessage = new Message(message.getUser() + " : " + message.getContent()); //Plain communication between users
        else if(message.type==1){ //New user joining the channel
        	finalmessage = new Message(message.getUser() + " joined the channel"); 
        }
        else if(message.type==2)finalmessage = new Message(message.getUser() + " left the channel"); //User leaving the channel
        return finalmessage;
    }
     
    
}