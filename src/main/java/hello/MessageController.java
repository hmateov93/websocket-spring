package hello;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Message message(MessageContent message) throws Exception {
        Thread.sleep(100); // simulated delay
        return new Message(message);
    }
}