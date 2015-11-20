package projects.PI.nodes.messages;

import sinalgo.nodes.messages.Message;

public class INFMessage extends Message {
	
	public int id;
	public int hopToSource;
	public int senderID;

	
	public INFMessage(int id, int hopToSource, int senderID) {
		this.id = id;
		this.hopToSource = hopToSource;
		this.senderID = senderID;
	}


	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return new INFMessage(this.id, this.hopToSource,this.senderID);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getHopToSink() {
		return hopToSource;
	}

	public void setHopToSink(int hopToSource) {
		this.hopToSource = hopToSource;
	}

	public int getSenderID() {
		return senderID;
	}

	public void setSenderID(int senderID) {
		this.senderID = senderID;
	}
	
}
