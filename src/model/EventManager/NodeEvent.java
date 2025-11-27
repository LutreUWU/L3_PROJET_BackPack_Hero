package model.EventManager;

import java.util.Objects;

public class NodeEvent {
	private String text;
	private NodeEvent choice1 = null;
	private NodeEvent choice2 = null;
	private Consequence consequence = null;
	
	
	public NodeEvent(String text) {
		Objects.requireNonNull(text);
		this.text = text;
	}
	
	public void setChoice1(NodeEvent new_choice1) {
		choice1 = new_choice1;
	}
	
	public void setChoice2(NodeEvent new_choice2) {
		choice2 = new_choice2;
	}
	
	public void setConseqeunce(Consequence c) {
		consequence = c;
	}
	
	public boolean isLeaf() {
		return (choice1 == null) && (choice2 == null);
	}
}