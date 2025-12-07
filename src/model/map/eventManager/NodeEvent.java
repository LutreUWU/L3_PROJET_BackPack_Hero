package model.map.eventManager;

public class NodeEvent {
	private String question;
	private String answer;
	private NodeEvent choice1 = null;
	private NodeEvent choice2 = null;
	private Consequence consequence = null;

	/**
	 * Constructor
	 * 
	 * @param answer
	 * @param question
	 */
	public NodeEvent(String answer, String question) {
		this.question = question;
		this.answer = answer;
	}

	/**
	 * Setter choice1
	 * 
	 * @param newChoice1
	 */
	public void setChoice1(NodeEvent newChoice1) {
		choice1 = newChoice1;
	}

	/**
	 * Setter choice2
	 * 
	 * @param newChoice2
	 */
	public void setChoice2(NodeEvent newChoice2) {
		choice2 = newChoice2;
	}

	/**
	 * Getter Choice1
	 * 
	 * @return a Node
	 */
	public NodeEvent getChoice1() {
		return choice1;
	}

	/**
	 * Getter Choice2
	 * 
	 * @return a Node
	 */
	public NodeEvent getChoice2() {
		return choice2;
	}

	/**
	 * Setter for consequence
	 * 
	 * @param c (a consequence)
	 */
	public void setConsequence(Consequence c) {
		consequence = c;
	}

	/**
	 * Getter for consequence
	 * 
	 * @return a consequence
	 */
	public Consequence getConsequence() {
		return consequence;
	}

	/**
	 * Getter for question
	 * 
	 * @return question (String)
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * Getter for answer
	 * 
	 * @return answer (String)
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * Is it the last choice ?
	 * 
	 * @return boolean
	 */
	public boolean isLastChoice() {
		return choice2 == null;
	}
}