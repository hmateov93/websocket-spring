package main;

public class Room {
	
	public static int idAc=1;
	
	public String name="Default Name";
	public int id=0;
	
	public Room(String name){
		this.name=name;
		this.id=idAc;
		idAc++;
	}
}
