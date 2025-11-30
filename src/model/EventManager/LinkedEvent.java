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
		var root = new NodeEvent("Vous rencontrez M. Revuz. \"Tu penses quoi du C?\"", null);
		// GOOD CHOICE
		var choice1 = new NodeEvent("Et tu en fais souvent ?", "Le C c'est trop cool !");
		root.setChoice1(choice1);
		
		var choice1_1 = createNodeWithConsequence("Oui j'en fait souvent !", floor, true, 1.5, "C'est bien continu ! J'ai même un petit cadeau pour toi !");
		choice1.setChoice1(choice1_1);
		var choice1_2 = createNodeWithConsequence("Non presque jamais...", floor, true, 1, "Dommage c'est vachement cool pourtant...");
		choice1.setChoice2(choice1_2);
		// BAD CHOICE
		var choice2 = new NodeEvent("A ce point la ? Pourquoi t'aime pas ?", "C'est vraiment le pire language ! Il sert a rien !");
		root.setChoice2(choice2);
		
		var choice2_1 = createNodeWithConsequence("J'arrive jamais a faire un malloc...", floor, false, 1, "C'est peut être le temps de relire ton cours...");
		choice2.setChoice1(choice2_1);
		var choice2_2 = createNodeWithConsequence("Par ce que je vous aime pas", floor, false, 1.5, "On peut dire que c'est réciproque");
		choice2.setChoice2(choice2_2);
		return root;
	}
	
	public NodeEvent createNodeWithConsequence(String answer, int floor, boolean is_good, double bonus, String final_answer) {
		var node = new NodeEvent(null, answer);
		node.setConsequence(new Consequence(floor, is_good, bonus, final_answer));
		return node;
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
