package hello;

public class Message {

    private String content;
    private User user;
    
    public Message(MessageContent message){
    	this.content=message.getContent();
    	this.user=message.getUser();
    }

    public String getMessage() {
        return user.getName() + ": " + content;
    }	
	
}
