package model.map.eventManager;

import java.util.Random;

import game.GameData;

public class LinkedEvent {
	
	private NodeEvent root;
	private NodeEvent initialRoot;
	private final int totalEvent = 1;
	
	
	/**
	 * Constructor for all EventRoom
	 * @param floor
	 * @param hero
	 */
	public LinkedEvent(int floor) {
		Random rand = new Random();
		var x = rand.nextInt(totalEvent) + 1;
		switch(x) {
			case 1 -> event1(floor);
			default -> {}
		};
		initialRoot = root;
	}
	
	/**
	 * Constructor for exit room
	 * @param data of the game
	 */
	public LinkedEvent(int floor, boolean isExit) {
		var question = floor == 3 ? "sortir du labyrinthe ?" : "monter à l'étage ?";
		root = new NodeEvent(null, "Souhaitez vous vous battre contre le boss pour " + question);
		
		var choiceOne = createNodeWithConsequence("Oui ! Je suis prêt !", floor, 1, "fight", "Bon courage !");
		root.setChoice1(choiceOne);
		
		var choiceTwo = createNodeWithConsequence("Non ! Je vais finir de me préparer...", floor, 1, "nothing", "Ca marche, prépare toi bien !");
		root.setChoice2(choiceTwo);
		initialRoot = root;
	}
	
	public void event1(int floor) {
	  root = new NodeEvent(null, "Vous rencontrez M. Revuz. \"Tu penses quoi du C?\"");
		// GOOD CHOICE
		var choiceOne = new NodeEvent("Le C c'est trop cool !", "Et tu en fais souvent ?");
		root.setChoice1(choiceOne);
		
		var choiceOneOne = createNodeWithConsequence("Oui j'en fait souvent !", floor, 1.5, "add_weapon", "C'est bien continu ! J'ai même un petit cadeau pour toi !\nVoici une arme!");
		choiceOne.setChoice1(choiceOneOne);
		var choiceOneTwo = createNodeWithConsequence("Non presque jamais...", floor, 1, "add_gold", "Dommage c'est vachement cool pourtant...\nJe viens de te donner de l'or");
		choiceOne.setChoice2(choiceOneTwo);
		// BAD CHOICE
		var choiceTwo = new NodeEvent("C'est vraiment le pire language ! Il sert a rien !", "A ce point la ? Pourquoi t'aime pas ?");
		root.setChoice2(choiceTwo);
		
		var choiceTwoOne = createNodeWithConsequence("J'arrive jamais a faire un malloc...", floor, 1, "nothing", "C'est peut être le temps de relire ton cours...\nM. Revuz te force à installer linux *aucun effet c'est gratuit*");
		choiceTwo.setChoice1(choiceTwoOne);
		var choiceTwoTwo = createNodeWithConsequence("Par ce que je vous aime pas", floor, 1.5, "sub_hp", "On peut dire que c'est réciproque...\n*Vous vous battez et perdez de la vie*");
		choiceTwo.setChoice2(choiceTwoTwo);
	}
	
	private NodeEvent createNodeWithConsequence(String answer, int floor, double bonus, String conseq, String lastAnswer) {
		var endNode = new NodeEvent("Mettre fin à l'évenement", null);
		var node = new NodeEvent(answer, lastAnswer);
		node.setChoice1(endNode);
		node.setConsequence(new Consequence(conseq, floor, bonus));
		return node;
	}
	
	public NodeEvent getRoot() {
		return root;
	}

	
	public void restartEvent() {
		root = initialRoot;
	}
	
	public void choose1(GameData data) {
		root = root.getChoice1();
		if (root.isLastChoice()) {
			root.getConsequence().applyConsequence(data); 
		}
	}

	public void choose2(GameData data) {
		root = root.getChoice2();
		if (root.isLastChoice()) {
			root.getConsequence().applyConsequence(data);
		}
	}
}
