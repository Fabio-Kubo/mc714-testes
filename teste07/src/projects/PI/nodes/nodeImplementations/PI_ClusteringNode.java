package projects.PI.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import projects.PI.nodes.messages.ClusteringMessage;
import projects.PI.nodes.messages.ElectionMessage;
import projects.PI.nodes.messages.INFMessage;
import projects.PI.nodes.timers.MessageTimer;
import sinalgo.configuration.Configuration;
import sinalgo.configuration.CorruptConfigurationEntryException;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.nodes.edges.Edge;

public class PI_ClusteringNode extends Node {
	private List<Integer> messagesReceived;
	private int hopToSource;
	private int soonList;
	private boolean ignoreTimer;
	int numberMessages;
	int currentLeader;

	public void startElection(){

		//trata temporizador
		ClusteringMessage msg = new ClusteringMessage(this.ID, ElectionMessage.TIME_EXPIRED);
		MessageTimer msgTimer = new MessageTimer(msg, 1);
		msgTimer.startRelative(1, this);
		ignoreTimer = false;

		//envia msg para os outros nos iniciando a eleicao
		ClusteringMessage msg2 = new ClusteringMessage(this.ID, ElectionMessage.ELECTION);
		MessageTimer msgTimer2 = new MessageTimer(msg2);
		msgTimer2.startRelative(1, this);
	}
		
	@Override
	public void handleMessages(Inbox inbox) {

		int sender;

		while (inbox.hasNext()) {
			Message msg = inbox.next();
			sender = inbox.getSender().ID;
			
			// Noh recebeu uma mensagem INF
			if (msg instanceof INFMessage) {
				// Verifica se e a primeira vez que o noh recebe INF
				
				if (!messagesReceived.contains(((INFMessage) msg).id)) {
					
					//marca que recebeu a msg
					messagesReceived.add(((INFMessage) msg).id);
					
					for (Edge edge : this.outgoingConnections) {
						
						//nao manda pro noh papai
						if(edge.endNode.ID != ((INFMessage) msg).senderID ){
							MessageTimer infMSG = new MessageTimer(msg, edge.endNode);
							infMSG.startRelative(1, this);
						}
			      	}
					
					if(messagesReceived.size() == numberMessages){
						this.setColor(Color.GREEN);
					}
					else{
						this.setColor(Color.YELLOW);
					}
				}
			}
			else if(msg instanceof ClusteringMessage){
				
				if(((ClusteringMessage) msg).getMessage() == ElectionMessage.ELECTION){
					
					System.out.println("ELECTION:" + ((ClusteringMessage) msg).getSenderID()
							+ " Recebeu:" + this.ID);
					
					//se o noh for superior
					if((this.ID > ((ClusteringMessage) msg).getSenderID())){
						ClusteringMessage clusteringMessage = new ClusteringMessage(this.ID, ElectionMessage.OK);
						MessageTimer msgTimer = new MessageTimer(clusteringMessage, ((ClusteringMessage) msg).getSenderID());
						msgTimer.startRelative(1, this);
					}
				}
				else if(((ClusteringMessage) msg).getMessage() == ElectionMessage.ELECTION_RESULTS){
					System.out.println("ELECTION_RESULTS:" + ((ClusteringMessage) msg).getSenderID()
							+ " Recebeu:" + this.ID);
					this.currentLeader = ((ClusteringMessage) msg).getSenderID();
				}
				else if(((ClusteringMessage) msg).getMessage() == ElectionMessage.TIME_EXPIRED){
					
					System.out.println("TIME_EXPIRED:" + ((ClusteringMessage) msg).getSenderID()
							+ " Recebeu:" + this.ID);
					
					if(((ClusteringMessage) msg).getSenderID() == this.ID){
						if(!ignoreTimer){
							this.currentLeader = this.ID;
							System.out.println("Virei lider:" + this.ID);
							this.setColor(Color.PINK);
						}
					}
				}
				else if(((ClusteringMessage) msg).getMessage() == ElectionMessage.OK){
					
					System.out.println("OK:" + ((ClusteringMessage) msg).getSenderID()
							+ " Recebeu:" + this.ID);
					
					//se o quem enviou tem mais poder
					if((((ClusteringMessage) msg).getSenderID()) > this.ID){
						this.setColor(Color.CYAN);
						this.ignoreTimer = true;
					}
				}
			}
		}

	}

	@Override
	public void init() {
		
		this.numberMessages = 0;
		this.messagesReceived = new LinkedList<Integer>();
		
		/*try {
			numberMessages = Configuration.getIntegerParameter("tarefa07/numberOfMessages");
		} catch (CorruptConfigurationEntryException e) {
			e.printStackTrace();
		}
		*/

		startElection();
			
		/*Considerando que o n� 1 tem a mensagem inf
		if (this.ID == 1) {
			this.setColor(Color.RED);
			this.hopToSource = 0;
			
			for(int i=0; i < numberMessages; i++){
				MessageTimer infMSG = new MessageTimer(new INFMessage(i, 1, this.ID));
				infMSG.startRelative(0.1, this);
			}
		}
		*/
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