package projects.PI.nodes.messages;

import sinalgo.nodes.messages.Message;

public class ClusteringMessage extends Message {
	
	private int senderID;
	private ElectionMessage message;

	
	public ClusteringMessage(int senderId, ElectionMessage msg) {
		this.senderID = senderId;
		this.message = msg;
	}

	public ElectionMessage getMessage() {
		return message;
	}

	public void setMessage(ElectionMessage message) {
		this.message = message;
	}

	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return new ClusteringMessage(this.senderID, this.message);
	}

	public int getSenderID() {
		return senderID;
	}

	public void setSenderID(int senderID) {
		this.senderID = senderID;
	}
	
}
