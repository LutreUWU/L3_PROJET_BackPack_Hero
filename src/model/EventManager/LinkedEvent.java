package model.EventManager;

import java.util.Random;

import game.GameData;
import model.Hero;

public class LinkedEvent {
	
	private NodeEvent root;
	private final int TOTAL_EVENT = 1;
	
	public LinkedEvent(int floor, Hero hero) {
		Random rand = new Random();
		var x = rand.nextInt(TOTAL_EVENT) + 1;
		this.root = switch(x) {
			case 1 -> event1(floor, hero);
			default -> null;
		};
	}
	
	public NodeEvent getRoot() {
		return root;
	}
	
	public NodeEvent event1(int floor, Hero hero) {
		var root = new NodeEvent("Vous rencontrez M. Revuz. Il vous demande ce que vous pensez du C:");
		
		var goodchoice = new NodeEvent("Le C c'est trop cool !");
		goodchoice.setConsequence(new Consequence(floor, true));
		root.setChoice1(goodchoice);
		
		var badchoice = new NodeEvent("C'est vraiment le pire language ! Il sert a rien !");
		badchoice.setConsequence(new Consequence(floor, false));
		root.setChoice2(badchoice);
		return root;
	}
	
	public void choose1(Hero hero) {
		root = root.getChoice1();
		if (root.isLeaf()) {
			root.getConsequence().apply(hero); 
			IO.println(root.getConsequence().getConsequenceString());
		}
	}

	public void choose2(Hero hero) {
		root = root.getChoice2();
		if (root.isLeaf()) {
			root.getConsequence().apply(hero);
			IO.println(root.getConsequence().getConsequenceString());
		}
	}
}
