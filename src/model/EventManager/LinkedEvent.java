package model.EventManager;

import game.GameData;

public class LinkedEvent {
	public NodeEvent event1(GameData data) {
		var root = new NodeEvent("Vous rencontrez M. Revuz.\nIl vous demande ce que vous pensez du C:");
		
		var goodchoice = new NodeEvent("Le C c'est trop cool !");
		goodchoice.setConseqeunce(new Consequence(1, data, true));
		root.setChoice1(goodchoice);
		
		var badchoice = new NodeEvent("C'est vraiment le pire language !\nIl sert a rien !");
		goodchoice.setConseqeunce(new Consequence(1, data, false));
		root.setChoice1(goodchoice);
		return root;
	}
}
