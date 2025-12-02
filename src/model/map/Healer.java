package model.map;

import java.util.ArrayList;
import java.util.List;

import model.Hero;
import model.XY;

public class Healer implements Room {
	int floor;
	private List<XY> accessible = new ArrayList<>();

	public Healer(int floor) {
		this.floor = floor;
	}
	
	public List<XY> getAccessible(){
		return accessible;
	}
	
	public void addAccessible(XY coord){
		accessible.add(coord);
	}
	
	public void healerUse(Hero hero) {
		hero.add("gold", floor * 3);
		hero.add("hp", floor * 6);
	}
}