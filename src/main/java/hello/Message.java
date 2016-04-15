package hello;

public class Message {

    private String content;
    private User user;
    
    public Message(MessageContent message){
    	this.content=message.getContent();
    }

    public String getContent() {
        return content;
    }	
	
}
