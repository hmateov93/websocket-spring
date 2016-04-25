package main;

public class Message {

    private String content;
    private String userlist;
    
    public Message(String message, String userlist){
    	this.content=message;
    	this.userlist=userlist;
    }

    public String getContent() {
        return content;
    }	
    
    public String getUserlist(){
    	return userlist;
    }
}
