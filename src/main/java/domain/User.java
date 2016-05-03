package domain;

public class User {
	
	private String type;
	private String name;
	private String password;
	
	public void setType(String type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
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
