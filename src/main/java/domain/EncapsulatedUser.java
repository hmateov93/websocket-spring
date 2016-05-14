package domain;

public class EncapsulatedUser {
	
	private String code;
	private User user;
	
	public EncapsulatedUser(String code, User user){
		this.code=code;
		this.user=user;
	}	
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}



}
