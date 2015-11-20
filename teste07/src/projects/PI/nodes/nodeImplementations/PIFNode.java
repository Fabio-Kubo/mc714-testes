package projects.PI.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

import projects.PI.nodes.messages.INFMessage;
import projects.PI.nodes.timers.MessageTimer;
import sinalgo.configuration.Configuration;
import sinalgo.configuration.CorruptConfigurationEntryException;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;

public class PIFNode extends Node {
	private List<Integer> messagesReceived;
	private int hopToSource;
	private int soonList;
	int numberMessages;
		
	@Override
	public void handleMessages(Inbox inbox) {
		// TODO Auto-generated method stub
		int sender;

		while (inbox.hasNext()) {
			Message msg = inbox.next();
			sender = inbox.getSender().ID;

			// Noh recebeu uma mensagem INF
			if (msg instanceof INFMessage) {
				// Verifica se e a primeira vez que o noh recebe INF
				
				if (!messagesReceived.contains(((INFMessage) msg).id)) {
					
					messagesReceived.add(((INFMessage) msg).id);
					
					MessageTimer infMSG = new MessageTimer(msg);
					infMSG.startRelative(1, this);
					
					if(messagesReceived.size() == numberMessages){
						this.setColor(Color.GREEN);
					}
					else{
						this.setColor(Color.YELLOW);
					}
				}
			}

		}

	}

	@Override
	public void init() {
		
		this.numberMessages = 0;
		this.messagesReceived = new LinkedList<Integer>();
		
		try {
			numberMessages = Configuration.getIntegerParameter("tarefa07/numberOfMessages");
		} catch (CorruptConfigurationEntryException e) {
			e.printStackTrace();
		}
			
		// Considerando que o n� 1 tem a mensagem inf
		if (this.ID == 1) {
			this.setColor(Color.RED);
			this.hopToSource = 0;
			
			for(int i=0; i < numberMessages; i++){
				MessageTimer infMSG = new MessageTimer(new INFMessage(i, 1, this.ID));
				infMSG.startRelative(0.1, this);
			}
		}
	}

	@Override
	public void postStep() {
		// TODO Auto-generated method stub
	}

	@Override
	public void preStep() {
		// TODO Auto-generated method stub
	}

	@Override
	public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
		// TODO Auto-generated method stub
		if (this.ID == 1)
			highlight = true;
		super.drawNodeAsDiskWithText(g, pt, highlight, Integer.toString(this.ID), 8, Color.WHITE);

	}

	@Override
	public void neighborhoodChange() {
		// TODO Auto-generated method stub

	}

	@Override
	public void checkRequirements() throws WrongConfigurationException {
		// TODO Auto-generated method stub

	}

}