package model.EventManager;

public class NodeEvent {
	private String question;
	private String answer;
	private NodeEvent choice1 = null;
	private NodeEvent choice2 = null;
	private Consequence consequence = null;
	
	
	public NodeEvent(String answer, String question) {
		this.question = question;
		this.answer = answer;
	}
	
	public void setChoice1(NodeEvent newChoice1) {
		choice1 = newChoice1;
	}
	
	public void setChoice2(NodeEvent newChoice2) {
		choice2 = newChoice2;
	}
	
	public NodeEvent getChoice1() {
		return choice1;
	}
	
	public NodeEvent getChoice2() {
		return choice2;
	}
	
	public void setConsequence(Consequence c) {
		consequence = c;
	}
	
	public Consequence getConsequence() {
		return consequence;
	}
	
	public String getQuestion() {
		return question;
	}
	
	public String getAnswer() {
		return answer;
	}
	
	/**
	 * Is it the last choice before the end ?
	 * @return
	 */
	public boolean isLastChoice() {
		return choice2 == null;
	}
}