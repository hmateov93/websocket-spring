package domain;

public class Message {

    private String content;
    private String userlist;
    private boolean roomActive;
    
    public Message(String message, String userlist, boolean roomActive){
    	this.content=message;
    	this.userlist=userlist;
    	this.roomActive=roomActive;
    }

    public String getContent() {
        return content;
    }	
    
    public String getUserlist(){
    	return userlist;
    }
    
    public boolean isRoomActive(){
    	return roomActive;
    }
}
