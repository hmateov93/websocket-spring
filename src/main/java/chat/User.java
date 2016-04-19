package chat;

public class User {
	
	public String type;
	public String name;
	public String password;
	
	public User(String type, String name, String password){
		this.type=type;
		this.name=name;
		this.password=password;
	}
	
	public String getType(){
		return type;
	}
	
	public String getName(){
		return name;
	}
	
	public String getPassword(){
		return password;
	}	
}
