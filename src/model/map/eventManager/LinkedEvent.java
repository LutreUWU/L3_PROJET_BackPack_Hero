package model.map.eventManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import game.GameData;
import model.EnemyRepository;
import model.monster.Chicken;
import model.monster.Enemy;
import model.monster.Robot;


/**
 * Represents a sequence or chain of game events.
 * 
 * LinkedEvent manages a collection of NodeEvent objects in a linked structure,
 * allowing the game to track and trigger a series of events in order.
 */
public class LinkedEvent {

	private NodeEvent root;
	private NodeEvent initialRoot;
	private final int TOTAL_EVENT = 3;

	/**
	 * Constructor to create all linkedEvent
	 * 
	 * @param floor			Current event
	 * @param whatEvent Which type of event
	 */
	public LinkedEvent(int floor, String whatEvent) {
		switch (whatEvent) {
		case "eventRoom" -> createEventForEventRoom(floor); // Random Event
		case "exitRoom" -> createEventForExitRoom(floor);
		case "lockedDoor" -> createEventForLockedRoom(floor);
		case "healerRoom" -> createEventForHealerRoom(floor);
		case "treasure" -> createEventForTreasure(floor);
		case "curse" -> createEventForCurse();
		}
		initialRoot = root;
	}
	
	/**
	 * Create event for a room
	 * 
	 * @param floor	Current floor
	 */
	private void createEventForEventRoom(int floor) {
		Random rand = new Random();
		var x = rand.nextInt(TOTAL_EVENT) + 1;
		switch (x) {
		case 1 -> event1(floor);
		case 2 -> event2(floor);
		case 3 -> event3(floor);
		default -> {
		}
		}
		;
	}
	
	/**
	 * Initializes the event sequence for a curse scenario in the game.
	 * 
	 * This method creates the root NodeEvent with a prompt asking the player 
	 * whether they want to place the curse in their backpack. 
	 * It then sets up two choices for the player:
	 *  - Choice 1: Accept the curse and trigger the "curseInBag" consequence.
	 *  - Choice 2: Decline the curse and trigger the "curseNotInBag" consequence.
	 * 
	 * Each choice node may contain additional messages or effects for the game logic.
	 * 
	 * This method is private because it is only meant to initialize the internal 
	 * event chain for curse-related interactions.
	 */
	private void createEventForCurse() {
		root = new NodeEvent(null, "Souhaitez vous mettre la malédiction dans le sac ?");
		var choiceOne = createNodeWithConsequence("Oui !", 0, 1, "curseInBag",
				"Reste à le placer dedans");
		root.setChoice1(choiceOne);

		var choiceTwo = createNodeWithConsequence("Non !", 0, 1, "curseNotInBag", "Ca marche ! Tu as perdu un peu de vie !");
		root.setChoice2(choiceTwo);
	}

	/**
	 * Create event for ExitRoom
	 * 
	 * @param floor Current floor
	 */
	private void createEventForExitRoom(int floor) {
		var question = floor == 3 ? "sortir du labyrinthe ?" : "monter à l'étage ?";
		root = new NodeEvent(null, "Souhaitez vous vous battre contre le boss pour " + question);
		
		var choiceOne = createNodeWithConsequence("Oui ! Je suis prêt !", floor, 1, new ArrayList<Enemy>(List.of(EnemyRepository.getOneBossLst(floor))), "Bon courage !");
		root.setChoice1(choiceOne);

		var choiceTwo = createNodeWithConsequence("Non ! Je vais finir de me préparer...", floor, 1, "nothing",
				"Ca marche, prépare toi bien !");
		root.setChoice2(choiceTwo);
	}

	/**
	 * Create event for LockedDoor
	 * 
	 * @param floor Current floor
	 */
	public void createEventForLockedRoom(int floor) {
		root = new NodeEvent(null, "Souhaitez vous utiliser la clef ?\n*Si vous en avez une*");

		var choiceOne = createNodeWithConsequence("Oui !", floor, 1, "key",
				"La porte s'est ouverte si vous aviez une clef !");
		root.setChoice1(choiceOne);

		var choiceTwo = createNodeWithConsequence("Non !", floor, 1, "nothing", "Ca marche ! Revient quand tu veux !");
		root.setChoice2(choiceTwo);
	}

	/**
	 * Create event to open the Treasure
	 * 
	 * @param floor Current floor
	 */
	public void createEventForTreasure(int floor) {
		root = new NodeEvent(null, "Souhaitez vous ouvrir le coffre ?");

		var choiceOne = createNodeWithConsequence("Oui !", floor, 1, "openTreasure", "Vous venez d'ouvrir le coffre !");
		root.setChoice1(choiceOne);

		var choiceTwo = createNodeWithConsequence("Non !", floor, 1, "nothing", "Ca marche ! Revient quand tu veux !");
		root.setChoice2(choiceTwo);
	}

	/**
	 * Create event for LockedDoor
	 * 
	 * @param floor Current floor
	 */
	public void createEventForHealerRoom(int floor) {
		root = new NodeEvent(null, "Souhaitez échanger " + floor * 5 + " pièces d'or contre " + floor * 15 + "HP ?");

		var choiceOne = createNodeWithConsequence("Oui !", floor, 1, "lifeExchangeGold",
				"Si vous aviez les pièces nécessaires,\nla transaction est effectué !");
		root.setChoice1(choiceOne);

		var choiceTwo = createNodeWithConsequence("Non !", floor, 1, "nothing", "Ca marche ! Revient quand tu veux !");
		root.setChoice2(choiceTwo);
	}

	/**
	 * Create an event with professor Revuz
	 * 
	 * @param floor Current floor
	 */
	private void event1(int floor) {
		root = new NodeEvent(null, "Vous rencontrez M. Revuz. \"Tu penses quoi du C?\"");
		// GOOD CHOICE
		var choiceOne = new NodeEvent("Le C c'est trop cool !", "Et tu en fais souvent ?");
		root.setChoice1(choiceOne);

		var choiceOneOne = createNodeWithConsequence("Oui j'en fait souvent !", floor, 1.5, "addWeapon",
				"C'est bien continu ! J'ai même un petit cadeau pour toi !\nVoici une arme!");
		choiceOne.setChoice1(choiceOneOne);
		var choiceOneTwo = createNodeWithConsequence("Non presque jamais...", floor, 1, "addGold",
				"Dommage c'est vachement cool pourtant...\nJe viens de te donner de l'or");
		choiceOne.setChoice2(choiceOneTwo);
		// BAD CHOICE
		var choiceTwo = new NodeEvent("C'est vraiment le pire language ! Il sert a rien !",
				"A ce point la ? Pourquoi t'aime pas ?");
		root.setChoice2(choiceTwo);

		var choiceTwoOne = createNodeWithConsequence("J'arrive jamais a faire un malloc...", floor, 1, "nothing",
				"C'est peut être le temps de relire ton cours...\nM. Revuz te force à installer linux *aucun effet c'est gratuit*");
		choiceTwo.setChoice1(choiceTwoOne);
		var choiceTwoTwo = createNodeWithConsequence("Par ce que je vous aime pas", floor, 1.5, new ArrayList<>(List.of(new Robot())),
				"On peut dire que c'est réciproque...\n*M. Revuz veut se battre !*");
		choiceTwo.setChoice2(choiceTwoTwo);
	}
	
	/**
	 * Create an event at the Crous
	 * 
	 * @param floor Current floor
	 */
	private void event2(int floor) {
		root = new NodeEvent(null, "Vous arrivez au Crous. \"T'es boursier?\"");
		
		var choiceOne = createNodeWithConsequence("Oui", floor, 1, "cookie",
				"C'est mon dernier cookie de la journée, tient cadeau !");
		root.setChoice1(choiceOne);
		
		var choiceTwo = createNodeWithConsequence("Non. Mettre un cookie dans le sac et partir", floor, 1, new ArrayList<Enemy>(List.of(new Chicken())), "\"EH ! Je t'ai vu!\" CrousChicken reprend le cookie et engage le combat");
		root.setChoice2(choiceTwo);
	}
	
	/**
	 * Create an event with a goblin
	 * 
	 * @param floor Current floor
	 */
	private void event3(int floor) {
		root = new NodeEvent(null, "Vous trouvez un goblin, que voulez vous faire ?");
		
		var choiceOne = createNodeWithConsequence("Lui demander gentillement de l'or", floor, 1, "addGold",
				"Il vous donne un peu d'or, la violence ne résout rien :)");
		root.setChoice1(choiceOne);
		
		var choiceTwo = createNodeWithConsequence("Coup de tête balayette", floor, 2, "addGold", "Vous l'assommez avant de dépouiller son or");
		root.setChoice2(choiceTwo);
	}
	
	

	/**
	 * Create the node with the consequences
	 * 
	 * @param answer
	 * @param floor
	 * @param bonus
	 * @param conseq
	 * @param lastAnswer
	 * @return a node
	 */
	private NodeEvent createNodeWithConsequence(String answer, int floor, double bonus, String conseq, String lastAnswer) {
		var endNode = new NodeEvent("Mettre fin à l'évenement", null);
		var node = new NodeEvent(answer, lastAnswer);
		node.setChoice1(endNode);
		node.setConsequence(new Consequence(conseq, floor, bonus));
		return node;
	}
	
	/**
	 * Create the node with the consequences for fight
	 * 
	 * @param answer
	 * @param floor
	 * @param bonus
	 * @param conseqEnemy (List of enemies)
	 * @param lastAnswer
	 * @return a node
	 */
	private NodeEvent createNodeWithConsequence(String answer, int floor, double bonus, List<Enemy> conseqEnemy, String lastAnswer) {
		var endNode = new NodeEvent("Mettre fin à l'évenement", null);
		var node = new NodeEvent(answer, lastAnswer);
		node.setChoice1(endNode);
		node.setConsequence(new Consequence(conseqEnemy, floor, bonus));
		return node;
	}

	/**
	 * Getter for root
	 * 
	 * @return a node (the root)
	 */
	public NodeEvent getRoot() {
		return root;
	}

	/**
	 * Restart the event
	 */
	public void restartEvent() {
		root = initialRoot;
	}

	/**
	 * Choose the choice1
	 * 
	 * @param data
	 */
	public void choose1(GameData data) {
		root = root.getChoice1();
		if (root.isLastChoice()) {
			root.getConsequence().applyConsequence(data);
		}
	}

	/**
	 * Choose the choice2
	 * 
	 * @param data
	 */
	public void choose2(GameData data) {
		root = root.getChoice2();
		if (root.isLastChoice()) {
			root.getConsequence().applyConsequence(data);
		}
	}
}
